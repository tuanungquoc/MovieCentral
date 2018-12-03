package com.cmpe275.finalproject.domain.order;

import java.util.List;

import org.bson.types.ObjectId;


public interface OrderDAL {
	List<OrderStats> getStatsFinanceByMonthAndYear(int year, int month) ;
	List<OrderStats> getStatsFinanceByLast12Months(int lyear, int lmonth) ;

}
