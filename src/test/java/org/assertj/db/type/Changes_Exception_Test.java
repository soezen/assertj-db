/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.db.type;

import javax.sql.DataSource;

import org.assertj.db.common.AbstractTest;
import org.assertj.db.common.DefaultDataSource;
import org.assertj.db.exception.AssertJDBException;
import org.junit.Test;

/**
 * Tests on the exceptions of Changes
 * 
 * @author Régis Pouiller
 * 
 */
public class Changes_Exception_Test extends AbstractTest {

  /**
   * This method should fail because the connection throw an exception when getting an object.
   */
  @Test(expected = AssertJDBException.class)
  public void should_fail_because_connection_throws_exception_when_getting_an_object() {
    DataSource ds = new DefaultDataSource();
    Table table = new Table(ds, "movi");
    Changes changes = new Changes().setTables(table);
    changes.setStartPointNow();
  }

  /**
   * This method should fail because the table is null.
   */
  @Test(expected = NullPointerException.class)
  public void should_fail_because_table_is_null() {
    new Changes().setTables((Table) null);
  }

  /**
   * This method should fail because the request is null.
   */
  @Test(expected = NullPointerException.class)
  public void should_fail_because_request_is_null() {
    new Changes().setRequest((Request) null);
  }

  /**
   * This method should fail because setting end point before start point.
   */
  @Test(expected = AssertJDBException.class)
  public void should_fail_because_end_before_start() {
    DataSource ds = new DefaultDataSource();
    Table table = new Table(ds, "test");
    Changes changes = new Changes().setTables(table);
    changes.setEndPointNow();
  }
}