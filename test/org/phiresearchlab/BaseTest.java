package org.phiresearchlab;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author <a href="mailto:jmrives@spiral-soft.com">Joel M. Rives</a>
 * Created on Oct 20, 2009
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations=
{
        "classpath:/META-INF/test/applicationContext.xml"
})
@TransactionConfiguration(transactionManager="transactionManager")
@Transactional
public abstract class BaseTest extends AbstractTransactionalJUnit4SpringContextTests
{

    @BeforeTransaction
    public void beforeTransaction()
    {
//        executeSqlScript("data/initial_data.sql", false);
    }

    @Before
    public void beforeTestWithinTransaction()
    {

    }

    @After
    public void afterTestWithinTransaction()
    {
       
    }

    @AfterTransaction
    public void afterTransaction()
    {

    }

    ///////////////////////////////////////////////////////////////
    // Support Methods
    ///////////////////////////////////////////////////////////////

}
