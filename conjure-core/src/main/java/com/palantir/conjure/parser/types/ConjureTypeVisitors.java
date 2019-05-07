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

import com.palantir.conjure.parser.types.builtin.AnyType;
import com.palantir.conjure.parser.types.builtin.BinaryType;
import com.palantir.conjure.parser.types.builtin.DateTimeType;
import com.palantir.conjure.parser.types.collect.ListType;
import com.palantir.conjure.parser.types.collect.MapType;
import com.palantir.conjure.parser.types.collect.OptionalType;
import com.palantir.conjure.parser.types.collect.SetType;
import com.palantir.conjure.parser.types.primitive.PrimitiveType;
import com.palantir.conjure.parser.types.reference.ForeignReferenceType;
import com.palantir.conjure.parser.types.reference.LocalReferenceType;

public final class ConjureTypeVisitors {

    public static final IsAnyConjureTypeVisitor IS_ANY = new IsAnyConjureTypeVisitor();
    public static final AnyConjureTypeVisitor ANY = new AnyConjureTypeVisitor();
    public static final IsListConjureTypeVisitor IS_LIST = new IsListConjureTypeVisitor();
    public static final ListConjureTypeVisitor LIST = new ListConjureTypeVisitor();
    public static final IsMapConjureTypeVisitor IS_MAP = new IsMapConjureTypeVisitor();
    public static final MapConjureTypeVisitor MAP = new MapConjureTypeVisitor();
    public static final IsOptionalConjureTypeVisitor IS_OPTIONAL = new IsOptionalConjureTypeVisitor();
    public static final OptionalConjureTypeVisitor OPTIONAL = new OptionalConjureTypeVisitor();
    public static final IsPrimitiveConjureTypeVisitor IS_PRIMITIVE = new IsPrimitiveConjureTypeVisitor();
    public static final PrimitiveConjureTypeVisitor PRIMITIVE = new PrimitiveConjureTypeVisitor();
    public static final IsLocalReferenceConjureTypeVisitor IS_LOCAL_REFERENCE =
            new IsLocalReferenceConjureTypeVisitor();
    public static final LocalReferenceConjureTypeVisitor LOCAL_REFERENCE = new LocalReferenceConjureTypeVisitor();
    public static final IsForeignReferenceConjureTypeVisitor IS_FOREIGN_REFERENCE =
            new IsForeignReferenceConjureTypeVisitor();
    public static final ForeignReferenceConjureTypeVisitor FOREIGN_REFERENCE = new ForeignReferenceConjureTypeVisitor();
    public static final IsSetConjureTypeVisitor IS_SET = new IsSetConjureTypeVisitor();
    public static final SetConjureTypeVisitor SET = new SetConjureTypeVisitor();
    public static final IsBinaryConjureTypeVisitor IS_BINARY = new IsBinaryConjureTypeVisitor();
    public static final BinaryConjureTypeVisitor BINARY = new BinaryConjureTypeVisitor();
    public static final IsDateTimeConjureTypeVisitor IS_DATE_TIME = new IsDateTimeConjureTypeVisitor();
    public static final DateTimeConjureTypeVisitor DATE_TIME = new DateTimeConjureTypeVisitor();

    private ConjureTypeVisitors() {}

    private static class IsAnyConjureTypeVisitor extends DefaultIsConjureTypeVisitor {
        @Override
        public Boolean visitAny(AnyType type) {
            return true;
        }
    }

    private static class AnyConjureTypeVisitor extends DefaultConjureTypeVisitor<AnyType> {
        @Override
        public AnyType visitAny(AnyType type) {
            return type;
        }
    }

    private static class IsListConjureTypeVisitor extends DefaultIsConjureTypeVisitor {
        @Override
        public Boolean visitList(ListType type) {
            return true;
        }
    }

    private static class ListConjureTypeVisitor extends DefaultConjureTypeVisitor<ListType> {
        @Override
        public ListType visitList(ListType type) {
            return type;
        }
    }

    private static class IsMapConjureTypeVisitor extends DefaultIsConjureTypeVisitor {
        @Override
        public Boolean visitMap(MapType type) {
            return true;
        }
    }

    private static class MapConjureTypeVisitor extends DefaultConjureTypeVisitor<MapType> {
        @Override
        public MapType visitMap(MapType type) {
            return type;
        }
    }

    private static class IsOptionalConjureTypeVisitor extends DefaultIsConjureTypeVisitor {
        @Override
        public Boolean visitOptional(OptionalType type) {
            return true;
        }
    }

    private static class OptionalConjureTypeVisitor extends DefaultConjureTypeVisitor<OptionalType> {
        @Override
        public OptionalType visitOptional(OptionalType type) {
            return type;
        }
    }

    private static class IsPrimitiveConjureTypeVisitor extends DefaultIsConjureTypeVisitor {
        @Override
        public Boolean visitPrimitive(PrimitiveType type) {
            return true;
        }
    }

    private static class PrimitiveConjureTypeVisitor extends DefaultConjureTypeVisitor<PrimitiveType> {
        @Override
        public PrimitiveType visitPrimitive(PrimitiveType type) {
            return type;
        }
    }

    private static class IsLocalReferenceConjureTypeVisitor extends DefaultIsConjureTypeVisitor {
        @Override
        public Boolean visitLocalReference(LocalReferenceType type) {
            return true;
        }
    }

    private static class LocalReferenceConjureTypeVisitor extends DefaultConjureTypeVisitor<LocalReferenceType> {
        @Override
        public LocalReferenceType visitLocalReference(LocalReferenceType type) {
            return type;
        }
    }

    private static class IsForeignReferenceConjureTypeVisitor extends DefaultIsConjureTypeVisitor {
        @Override
        public Boolean visitForeignReference(ForeignReferenceType type) {
            return true;
        }
    }

    private static class ForeignReferenceConjureTypeVisitor extends DefaultConjureTypeVisitor<ForeignReferenceType> {
        @Override
        public ForeignReferenceType visitForeignReference(ForeignReferenceType type) {
            return type;
        }
    }

    private static class IsSetConjureTypeVisitor extends DefaultIsConjureTypeVisitor {
        @Override
        public Boolean visitSet(SetType type) {
            return true;
        }
    }

    private static class SetConjureTypeVisitor extends DefaultConjureTypeVisitor<SetType> {
        @Override
        public SetType visitSet(SetType type) {
            return type;
        }
    }

    private static class IsBinaryConjureTypeVisitor extends DefaultIsConjureTypeVisitor {
        @Override
        public Boolean visitBinary(BinaryType type) {
            return true;
        }
    }

    private static class BinaryConjureTypeVisitor extends DefaultConjureTypeVisitor<BinaryType> {
        @Override
        public BinaryType visitBinary(BinaryType type) {
            return type;
        }
    }

    private static class IsDateTimeConjureTypeVisitor extends DefaultIsConjureTypeVisitor {
        @Override
        public Boolean visitDateTime(DateTimeType type) {
            return true;
        }
    }

    private static class DateTimeConjureTypeVisitor extends DefaultConjureTypeVisitor<DateTimeType> {
        @Override
        public DateTimeType visitDateTime(DateTimeType type) {
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
}
