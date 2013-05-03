/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/
package eu.trentorise.smartcampus.corsi.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.trentorise.smartcampus.corsi.storage.CorsiStorage;
import eu.trentorise.smartcampus.presentation.common.exception.DataException;
import eu.trentorise.smartcampus.presentation.common.exception.NotFoundException;
import eu.trentorise.smartcampus.presentation.data.BasicObject;

@Component
public class CorsiManager {

	@Autowired
	CorsiStorage storage;

	public void create(BasicObject notification) throws DataException,
			NotFoundException {
		storage.storeObject(notification);

	}

	public boolean delete(String id) throws NotFoundException, DataException {
		storage.deleteObject(storage.getObjectById(id, BasicObject.class));
		return true;
	}

	public BasicObject getById(String id) throws NotFoundException,
			DataException {
		return storage.getObjectById(id, BasicObject.class);
	}

}
