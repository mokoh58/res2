package com.dao;

import java.util.ArrayList;
import com.objects.Tags;
import com.objects.Result;

public interface TagsDAO {
	String createTags(Tags tag);

    void deleteTags(String resId);

    ArrayList<Tags> getTags(String resId);
}
