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

package com.palantir.conjure.parser.types;

import com.palantir.conjure.parser.types.complex.EnumTypeDefinition;
import com.palantir.conjure.parser.types.complex.ObjectTypeDefinition;
import com.palantir.conjure.parser.types.complex.UnionTypeDefinition;
import com.palantir.conjure.parser.types.reference.AliasTypeDefinition;

public final class TypeDefinitionVisitors {

    public static final IsObjectTypeDefinitionVisitor IS_OBJECT = new IsObjectTypeDefinitionVisitor();
    public static final ObjectTypeDefinitionVisitor OBJECT = new ObjectTypeDefinitionVisitor();
    public static final IsUnionTypeDefinitionVisitor IS_UNION = new IsUnionTypeDefinitionVisitor();
    public static final UnionTypeDefinitionVisitor UNION = new UnionTypeDefinitionVisitor();
    public static final IsAliasTypeDefinitionVisitor IS_ALIAS = new IsAliasTypeDefinitionVisitor();
    public static final AliasTypeDefinitionVisitor ALIAS = new AliasTypeDefinitionVisitor();
    public static final IsEnumTypeDefinitionVisitor IS_ENUM = new IsEnumTypeDefinitionVisitor();
    public static final EnumTypeDefinitionVisitor ENUM = new EnumTypeDefinitionVisitor();

    private TypeDefinitionVisitors() {}

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

    private static class IsEnumTypeDefinitionVisitor extends DefaultIsTypeDefinitionVisitor {
        @Override
        public Boolean visit(EnumTypeDefinition def) {
            return true;
        }
    }

    private static class EnumTypeDefinitionVisitor extends DefaultTypeDefinitionVisitor<EnumTypeDefinition> {
        @Override
        public EnumTypeDefinition visit(EnumTypeDefinition def) {
            return def;
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
