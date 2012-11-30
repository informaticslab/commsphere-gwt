package org.phiresearchlab.commsphere.dao;

import org.junit.After;
import org.junit.Before;
import org.phiresearchlab.BaseTest;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;

/**
 *
 * @author <a href="mailto:jmrives@spiral-soft.com">Joel M. Rives</a>
 * Created on Oct 20, 2009
 *
 */

public abstract class BaseDaoTest extends BaseTest
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
