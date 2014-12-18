package com.truly.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MyUtils {

	/**
	 * 可以将所有Bean List转换成对应的Map List 主要用于填充SimpleAdapter
	 * 
	 * @param list
	 *            需要转换的实体对象List
	 * @return 已经转换好的Map映射List
	 */
	public List<HashMap<String, Object>> ConvertToMapList(List<?> list,String[] segments) {

		if (list.isEmpty())
			return null;
		Class<?> T = list.get(0).getClass();
		Field[] fields = T.getDeclaredFields();
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();

		for (Object obj : list) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (Field f : fields) {
				
				//如果字段名不为空而且字段名不包含当前field，则跳过继续
				if(segments!=null && !Arrays.asList(segments).contains(f.getName())){
					continue;					
				}
				
				Object fieldValue = null;
				try {
					Method method = T.getDeclaredMethod("get"
							+ f.getName().substring(0, 1)
									.toUpperCase(Locale.getDefault())
							+ f.getName().substring(1));
					try {
						fieldValue = method.invoke(obj);
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				} catch (NoSuchMethodException e) {
					//如果没有这个field对应的getter方法，则跳过然后继续
					e.printStackTrace();
					continue;
				}
				map.put(f.getName(), fieldValue);
			}
			result.add(map);
		}
		return result;
	}

}
