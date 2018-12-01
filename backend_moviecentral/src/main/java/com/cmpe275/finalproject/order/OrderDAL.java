package com.cmpe275.finalproject.order;

import java.util.List;

import org.bson.types.ObjectId;


public interface OrderDAL {
	List<OrderStats> getStatsFinanceByMonthAndYear(int year, int month) ;

}
