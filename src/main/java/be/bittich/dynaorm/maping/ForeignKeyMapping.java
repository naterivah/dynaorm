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
package be.bittich.dynaorm.maping;

import be.bittich.dynaorm.core.TableColumn;
import java.io.Serializable;

/**
 *
 * @author Nordine
 */
public class ForeignKeyMapping<T> implements Serializable {

    private static final long serialVersionUID = 333302193062282418L;

    private String id;
    private String idMappedBy;

  

    
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the idMappedBy
     */
    public String getIdMappedBy() {
        return idMappedBy;
    }

    /**
     * @param idMappedBy the idMappedBy to set
     */
    public void setIdMappedBy(String idMappedBy) {
        this.idMappedBy = idMappedBy;
    }



    public ForeignKeyMapping() {
    }

    public ForeignKeyMapping(String id, String idMappedBy) {
        this.id = id;
        this.idMappedBy = idMappedBy;
    }

}
