/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.palantir.conjure.defs.validator;

import com.google.common.collect.Sets;
import com.palantir.conjure.parser.ConjureSourceFile;
import com.palantir.conjure.parser.types.ConjureType;
import com.palantir.conjure.parser.types.ConjureTypeVisitors;
import com.palantir.conjure.parser.types.TypeDefinitionVisitors;
import com.palantir.conjure.parser.types.names.Namespace;
import com.palantir.conjure.parser.types.names.TypeName;
import com.palantir.conjure.parser.types.reference.ForeignReferenceType;
import com.palantir.conjure.parser.types.reference.LocalReferenceType;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import java.util.Set;
import java.util.stream.Collectors;

public final class ObjectImportValidator {

    private ObjectImportValidator() {}

    public static void validate(ConjureSourceFile conjureSourceFile) {
        Set<ForeignReferenceType> usedForeignReferences = Sets.newHashSet();
        Set<LocalReferenceType> usedLocalReferences = Sets.newHashSet();

        // Gather used reference types from service definitions
        conjureSourceFile.services().values().forEach(sd ->
                sd.endpoints().values().forEach(ed ->
                        ed.args().values().forEach(ad ->
                                addIfReferenceTypeToSet(ad.type(), usedForeignReferences, usedLocalReferences))));

        // Gather used reference types from object definitions
        conjureSourceFile.types().definitions().objects().values().forEach(td -> {
            if (td.visit(TypeDefinitionVisitors.IS_OBJECT)) {
                td.visit(TypeDefinitionVisitors.OBJECT).fields().values().forEach(fd ->
                        addIfReferenceTypeToSet(fd.type(), usedForeignReferences, usedLocalReferences));
            } else if (td.visit(TypeDefinitionVisitors.IS_UNION)) {
                td.visit(TypeDefinitionVisitors.UNION).union().values().forEach(fd ->
                        addIfReferenceTypeToSet(fd.type(), usedForeignReferences, usedLocalReferences));
            } else if (td.visit(TypeDefinitionVisitors.IS_ALIAS)) {
                addIfReferenceTypeToSet(td.visit(TypeDefinitionVisitors.ALIAS).alias(), usedForeignReferences,
                        usedLocalReferences);
            }
        });

        // Compare imported namespaces against namespaces in used foreign references
        Sets.SetView<Namespace> unusedNamespaces = Sets.difference(
                conjureSourceFile.types().conjureImports().keySet(),
                usedForeignReferences.stream()
                        .map(ForeignReferenceType::namespace)
                        .collect(Collectors.toSet()));
        Preconditions.checkState(unusedNamespaces.isEmpty(), "Unused namespaces have been imported",
                SafeArg.of("unusedNamespaces", unusedNamespaces));

        // Compare imported external type names against type names in all local references
        Sets.SetView<TypeName> unusedExternalTypeNames = Sets.difference(
                conjureSourceFile.types().imports().keySet(),
                usedLocalReferences.stream().map(LocalReferenceType::type).collect(Collectors.toSet()));
        Preconditions.checkState(unusedExternalTypeNames.isEmpty(), "Unused external types have been imported",
                SafeArg.of("unusedExternalTypes", unusedExternalTypeNames));
    }

    private static void addIfReferenceTypeToSet(
            ConjureType type,
            Set<ForeignReferenceType> foreign,
            Set<LocalReferenceType> local) {
        if (type.visit(ConjureTypeVisitors.IS_FOREIGN_REFERENCE)) {
            foreign.add(type.visit(ConjureTypeVisitors.FOREIGN_REFERENCE));
        } else if (type.visit(ConjureTypeVisitors.IS_LOCAL_REFERENCE)) {
            local.add(type.visit(ConjureTypeVisitors.LOCAL_REFERENCE));
        }
    }






}
