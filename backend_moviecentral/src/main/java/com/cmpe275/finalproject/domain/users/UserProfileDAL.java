package com.cmpe275.finalproject.domain.users;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UserProfileDAL {
	Page<UserProfile> searchProfileByKeyWord(List<String>keywords,Pageable pageable);

}
