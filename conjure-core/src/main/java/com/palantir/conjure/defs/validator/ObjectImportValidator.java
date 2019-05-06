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
import com.palantir.conjure.parser.types.ConjureTypeVisitor;
import com.palantir.conjure.parser.types.TypeDefinitionVisitor;
import com.palantir.conjure.parser.types.builtin.AnyType;
import com.palantir.conjure.parser.types.builtin.BinaryType;
import com.palantir.conjure.parser.types.builtin.DateTimeType;
import com.palantir.conjure.parser.types.collect.ListType;
import com.palantir.conjure.parser.types.collect.MapType;
import com.palantir.conjure.parser.types.collect.OptionalType;
import com.palantir.conjure.parser.types.collect.SetType;
import com.palantir.conjure.parser.types.complex.EnumTypeDefinition;
import com.palantir.conjure.parser.types.complex.ObjectTypeDefinition;
import com.palantir.conjure.parser.types.complex.UnionTypeDefinition;
import com.palantir.conjure.parser.types.names.Namespace;
import com.palantir.conjure.parser.types.names.TypeName;
import com.palantir.conjure.parser.types.primitive.PrimitiveType;
import com.palantir.conjure.parser.types.reference.AliasTypeDefinition;
import com.palantir.conjure.parser.types.reference.ForeignReferenceType;
import com.palantir.conjure.parser.types.reference.LocalReferenceType;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import java.util.Set;
import java.util.stream.Collectors;

public final class ObjectImportValidator {

    private static final IsForeignReferenceTypeVisitor IS_FOREIGN_REFERENCE = new IsForeignReferenceTypeVisitor();
    private static final ForeignReferenceTypeVisitor FOREIGN_REFERENCE = new ForeignReferenceTypeVisitor();
    private static final IsLocalReferenceTypeVisitor IS_LOCAL_REFERENCE = new IsLocalReferenceTypeVisitor();
    private static final LocalReferenceTypeVisitor LOCAL_REFERENCE = new LocalReferenceTypeVisitor();

    private static final IsObjectTypeDefinitionVisitor IS_OBJECT = new IsObjectTypeDefinitionVisitor();
    private static final ObjectTypeDefinitionVisitor OBJECT = new ObjectTypeDefinitionVisitor();
    private static final IsUnionTypeDefinitionVisitor IS_UNION = new IsUnionTypeDefinitionVisitor();
    private static final UnionTypeDefinitionVisitor UNION = new UnionTypeDefinitionVisitor();
    private static final IsAliasTypeDefinitionVisitor IS_ALIAS = new IsAliasTypeDefinitionVisitor();
    private static final AliasTypeDefinitionVisitor ALIAS = new AliasTypeDefinitionVisitor();

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
            if (td.visit(IS_OBJECT)) {
                td.visit(OBJECT).fields().values().forEach(fd ->
                        addIfReferenceTypeToSet(fd.type(), usedForeignReferences, usedLocalReferences));
            } else if (td.visit(IS_UNION)) {
                td.visit(UNION).union().values().forEach(fd ->
                        addIfReferenceTypeToSet(fd.type(), usedForeignReferences, usedLocalReferences));
            } else if (td.visit(IS_ALIAS)) {
                addIfReferenceTypeToSet(td.visit(ALIAS).alias(), usedForeignReferences, usedLocalReferences);
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
        if (type.visit(IS_FOREIGN_REFERENCE)) {
            foreign.add(type.visit(FOREIGN_REFERENCE));
        } else if (type.visit(IS_LOCAL_REFERENCE)) {
            local.add(type.visit(LOCAL_REFERENCE));
        }
    }

    private static class IsObjectTypeDefinitionVisitor extends DefaultIsTypeDefinitionVisitor {
        @Override
        public Boolean visit(ObjectTypeDefinition def) {
            return true;
        }
    }

    private static class ObjectTypeDefinitionVisitor extends DefaultTypeDefinitionVisitor<ObjectTypeDefinition> {
        @Override
        public ObjectTypeDefinition visit(ObjectTypeDefinition def) {
            return def;
        }
    }

    private static class IsUnionTypeDefinitionVisitor extends DefaultIsTypeDefinitionVisitor {
        @Override
        public Boolean visit(UnionTypeDefinition def) {
            return true;
        }
    }

    private static class UnionTypeDefinitionVisitor extends DefaultTypeDefinitionVisitor<UnionTypeDefinition> {
        @Override
        public UnionTypeDefinition visit(UnionTypeDefinition def) {
            return def;
        }
    }

    private static class IsAliasTypeDefinitionVisitor extends DefaultIsTypeDefinitionVisitor {
        @Override
        public Boolean visit(AliasTypeDefinition def) {
            return true;
        }
    }

    private static class AliasTypeDefinitionVisitor extends DefaultTypeDefinitionVisitor<AliasTypeDefinition> {
        @Override
        public AliasTypeDefinition visit(AliasTypeDefinition def) {
            return def;
        }
    }

    private static class IsLocalReferenceTypeVisitor extends DefaultIsConjureTypeVisitor {
        @Override
        public Boolean visitLocalReference(LocalReferenceType type) {
            return true;
        }
    }

    private static class LocalReferenceTypeVisitor extends DefaultConjureTypeVisitor<LocalReferenceType> {
        @Override
        public LocalReferenceType visitLocalReference(LocalReferenceType type) {
            return type;
        }
    }

    private static class IsForeignReferenceTypeVisitor extends DefaultIsConjureTypeVisitor {
        @Override
        public Boolean visitForeignReference(ForeignReferenceType type) {
            return true;
        }
    }

    private static class ForeignReferenceTypeVisitor extends DefaultConjureTypeVisitor<ForeignReferenceType> {
        @Override
        public ForeignReferenceType visitForeignReference(ForeignReferenceType type) {
            return type;
        }
    }

    private static class DefaultIsConjureTypeVisitor implements ConjureTypeVisitor<Boolean> {
        @Override
        public Boolean visitAny(AnyType type) {
            return false;
        }

        @Override
        public Boolean visitList(ListType type) {
            return false;
        }

        @Override
        public Boolean visitMap(MapType type) {
            return false;
        }

        @Override
        public Boolean visitOptional(OptionalType type) {
            return false;
        }

        @Override
        public Boolean visitPrimitive(PrimitiveType type) {
            return false;
        }

        @Override
        public Boolean visitLocalReference(LocalReferenceType type) {
            return false;
        }

        @Override
        public Boolean visitForeignReference(ForeignReferenceType type) {
            return false;
        }

        @Override
        public Boolean visitSet(SetType type) {
            return false;
        }

        @Override
        public Boolean visitBinary(BinaryType type) {
            return false;
        }

        @Override
        public Boolean visitDateTime(DateTimeType type) {
            return false;
        }
    }

    private static class DefaultConjureTypeVisitor<T> implements ConjureTypeVisitor<T> {
        @Override
        public T visitAny(AnyType type) {
            throw new IllegalStateException("Unknown type: " + type);
        }

        @Override
        public T visitList(ListType type) {
            throw new IllegalStateException("Unknown type: " + type);
        }

        @Override
        public T visitMap(MapType type) {
            throw new IllegalStateException("Unknown type: " + type);
        }

        @Override
        public T visitOptional(OptionalType type) {
            throw new IllegalStateException("Unknown type: " + type);
        }

        @Override
        public T visitPrimitive(PrimitiveType type) {
            throw new IllegalStateException("Unknown type: " + type);
        }

        @Override
        public T visitLocalReference(LocalReferenceType type) {
            throw new IllegalStateException("Unknown type: " + type);
        }

        @Override
        public T visitForeignReference(ForeignReferenceType type) {
            throw new IllegalStateException("Unknown type: " + type);
        }

        @Override
        public T visitSet(SetType type) {
            throw new IllegalStateException("Unknown type: " + type);
        }

        @Override
        public T visitBinary(BinaryType type) {
            throw new IllegalStateException("Unknown type: " + type);
        }

        @Override
        public T visitDateTime(DateTimeType type) {
            throw new IllegalStateException("Unknown type: " + type);
        }
    }

    private static class DefaultIsTypeDefinitionVisitor implements TypeDefinitionVisitor<Boolean> {
        @Override
        public Boolean visit(AliasTypeDefinition def) {
            return false;
        }

        @Override
        public Boolean visit(EnumTypeDefinition def) {
            return false;
        }

        @Override
        public Boolean visit(ObjectTypeDefinition def) {
            return false;
        }

        @Override
        public Boolean visit(UnionTypeDefinition def) {
            return false;
        }
    }

    private static class DefaultTypeDefinitionVisitor<T> implements TypeDefinitionVisitor<T> {
        @Override
        public T visit(AliasTypeDefinition def) {
            throw new IllegalStateException("Unknown type: " + def);
        }

        @Override
        public T visit(EnumTypeDefinition def) {
            throw new IllegalStateException("Unknown type: " + def);
        }

        @Override
        public T visit(ObjectTypeDefinition def) {
            throw new IllegalStateException("Unknown type: " + def);
        }

        @Override
        public T visit(UnionTypeDefinition def) {
            throw new IllegalStateException("Unknown type: " + def);
        }
    }
}
