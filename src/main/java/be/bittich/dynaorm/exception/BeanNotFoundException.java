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
package be.bittich.dynaorm.exception;

/**
 *
 * @author Nordine
 */
public class BeanNotFoundException extends Exception {

    private static final long serialVersionUID = -5784646061904407507L;

    public BeanNotFoundException(String msg) {
        super(msg);
    }

    public BeanNotFoundException() {
        super();
    }

}
