/*
 * Copyright 2014 Nordine.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.bittich.dynaorm.entity;

/**
 *
 * @author Nordine
 * @param <T>
 */
public class BasicPrimaryKey<T> implements PrimaryKey {

    private static final long serialVersionUID = 3968117105237930690L;
    private final T id;
    private final String label;
    private final Class<T> clazz;

    public BasicPrimaryKey(T t, String label) {
        this.id = t;
        this.label = label;
        this.clazz = (Class<T>) t.getClass();

    }

    @Override
    public T getIdValue() {
        return id;
    }

    @Override
    public String getIdLabel() {
        return label;
    }

    @Override
    public Class<T> getClazz() {

        return clazz;
    }
}
