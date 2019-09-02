package com.silverbars;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.silverbars.manager.MergeKeyTest;
import com.silverbars.manager.OrderManagerTest;
import com.silverbars.order.OrderTest;

@RunWith(Suite.class)
@SuiteClasses({ MergeKeyTest.class, OrderManagerTest.class, OrderTest.class })
public class AllTests {

}
