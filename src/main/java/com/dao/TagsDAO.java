package com.dao;

import java.util.ArrayList;
import java.util.List;

import com.objects.Tags;
import com.objects.Result;

public interface TagsDAO {
	String createTags(Tags tag);

    void deleteTags(String resId);

    ArrayList<Tags> getTags(String resId);

    List<String> getStringTags(String resId);

    List<String> getRestaurants(String tag);
}
