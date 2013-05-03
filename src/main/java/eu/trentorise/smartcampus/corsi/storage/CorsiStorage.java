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
package eu.trentorise.smartcampus.corsi.storage;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import eu.trentorise.smartcampus.presentation.common.exception.DataException;
import eu.trentorise.smartcampus.presentation.data.BasicObject;
import eu.trentorise.smartcampus.presentation.storage.sync.mongo.BasicObjectSyncMongoStorage;

public class CorsiStorage extends BasicObjectSyncMongoStorage {

	public CorsiStorage(MongoOperations mongoTemplate) {
		super(mongoTemplate);
	}

	public <T extends BasicObject> void deleteObjectPermanently(T object)
			throws DataException {
		mongoTemplate.remove(
				Query.query(Criteria.where("id").is(object.getId())),
				getObjectClass());
	}

	public <T extends BasicObject> void deleteObjectsPermanently(Class<T> cls,
			String user) throws DataException {
		mongoTemplate.remove(
				Query.query(Criteria.where("user").is(user).and("type")
						.is(cls.getCanonicalName())), getObjectClass());
	}

}
