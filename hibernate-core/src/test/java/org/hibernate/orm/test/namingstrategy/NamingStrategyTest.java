/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.orm.test.namingstrategy;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;

import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.orm.junit.BaseSessionFactoryFunctionalTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Emmanuel Bernard
 * @author Lukasz Antoniak
 */
public class NamingStrategyTest extends BaseSessionFactoryFunctionalTest {

	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] {
				Item.class
		};
	}

	@Override
	protected String[] getOrmXmlFiles() {
		return new String[] {
				"org/hibernate/orm/test/namingstrategy/Customers.hbm.xml"
		};
	}

	@Override
	protected void applySettings(StandardServiceRegistryBuilder builder) {
		builder.applySetting( AvailableSettings.IMPLICIT_NAMING_STRATEGY, TestNamingStrategy.class.getName() );
		builder.applySetting( AvailableSettings.PHYSICAL_NAMING_STRATEGY, TestNamingStrategy.class.getName() );
	}


	@Test
	public void testDatabaseColumnNames() {
		PersistentClass classMapping = getMetadata().getEntityBinding( Customers.class.getName() );
		Column stateColumn = (Column) classMapping.getProperty( "specified_column" ).getColumnIterator().next();
		assertEquals( "CN_specified_column", stateColumn.getName() );
	}

	@Test
	@TestForIssue(jiraKey = "HHH-5848")
	public void testDatabaseTableNames() {
		PersistentClass classMapping = getMetadata().getEntityBinding( Item.class.getName() );
		Column secTabColumn = (Column) classMapping.getProperty( "specialPrice" ).getColumnIterator().next();
		assertEquals( "TAB_ITEMS_SEC", secTabColumn.getValue().getTable().getName() );
		Column tabColumn = (Column) classMapping.getProperty( "price" ).getColumnIterator().next();
		assertEquals( "TAB_ITEMS", tabColumn.getValue().getTable().getName() );
	}
}
