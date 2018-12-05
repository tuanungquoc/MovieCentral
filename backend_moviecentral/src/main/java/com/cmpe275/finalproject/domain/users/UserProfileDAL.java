package com.cmpe275.finalproject.domain.users;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cmpe275.finalproject.domain.order.OrderStats;


public interface UserProfileDAL {
	Page<UserProfile> searchProfileByKeyWord(List<String>keywords,Pageable pageable);
	List<UserProfileStats> getUniqueSubcribedUsersLast12Months(int year, int month);
	List<UserProfileStats> getUniqueSubcribedUsersByMonthAndYear(int year, int month);
}
