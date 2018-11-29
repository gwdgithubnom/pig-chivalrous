/*
package org.gjgr.tools.newJson;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import Console;

*/
/**
 * JSONObject单元测试
 *
 * @author Looly
 * <p>
 * <p>
 * 测试Bean
 * @author Looly
 * <p>
 * <p>
 * 测试子Bean
 * @author Looly
 * <p>
 * 测试Bean
 * @author Looly
 * <p>
 * <p>
 * 测试子Bean
 * @author Looly
 * <p>
 * 测试Bean
 * @author Looly
 * <p>
 * <p>
 * 测试子Bean
 * @author Looly
 *
 * 测试Bean
 * @author Looly
 *
 *
 * 测试子Bean
 * @author Looly
 *//*

public class JSONObjectTest {
	
	@Test
	public void putAllTest(){
		JSONObject json1 = JsonUtil.createObj();
		json1.put("a", "value1");
		json1.put("b", "value2");
		json1.put("c", "value3");
		
		JSONObject json2 = JsonUtil.createObj();
		json2.put("a", "value21");
		json2.put("b", "value22");
		
		//putAll操作会覆盖相同key的值，因此a,b两个key的值改变，c的值不变
		json1.putAll(json2);
		
		Assert.assertEquals(json1.get("a"), "value21");
		Assert.assertEquals(json1.get("b"), "value22");
		Assert.assertEquals(json1.get("c"), "value3");
		
		
	}
	
	@Test
	public void parseTest(){
		String jsonStr = "{\"b\":\"value2\",\"c\":\"value3\",\"a\":\"value1\"}";
		JSONObject jsonObject = JsonUtil.jsonObject(jsonStr);
		Assert.assertEquals(jsonObject.get("a"), "value1");
		Assert.assertEquals(jsonObject.get("b"), "value2");
		
		Assert.assertEquals(jsonObject.toString(), jsonStr);
		
	}
	
	@Test
	public void toBeanTest(){
		JSONObject subJson = JsonUtil.createObj().put("value1", "strValue1").put("value2", "234");
		JSONObject newJson = JsonUtil.createObj()
			.put("strValue", "strTest")
			.put("intValue", 123)
			.put("beanValue", subJson)
			.put("list", JsonUtil.createArray().put("a").put("b"));
		
		TestBean bean = newJson.bean(TestBean.class);
		Console.log(bean);
	}
	
	*/
/**
 * 测试Bean
 * @author Looly
 *
 *//*

	public static class TestBean{
		private String strValue;
		private int intValue;
		private subBean beanValue;
		private List<String> list;
		
		public String getStrValue() {
			return strValue;
		}
		public void setStrValue(String strValue) {
			this.strValue = strValue;
		}
		public int getIntValue() {
			return intValue;
		}
		public void setIntValue(int intValue) {
			this.intValue = intValue;
		}
		public subBean getBeanValue() {
			return beanValue;
		}
		public void setBeanValue(subBean beanValue) {
			this.beanValue = beanValue;
		}
		public List<String> getList() {
			return list;
		}
		public void setList(List<String> list) {
			this.list = list;
		}
		@Override
		public String toString() {
			return "TestBean [strValue=" + strValue + ", intValue=" + intValue + ", beanValue=" + beanValue + ", list=" + list + "]";
		}
	}
	
	*/
/**
 * 测试子Bean
 * @author Looly
 *
 *//*

	public static class subBean{
		private String value1;
		private BigDecimal value2;
		
		public String getValue1() {
			return value1;
		}
		public void setValue1(String value1) {
			this.value1 = value1;
		}
		public BigDecimal getValue2() {
			return value2;
		}
		public void setValue2(BigDecimal value2) {
			this.value2 = value2;
		}
		@Override
		public String toString() {
			return "subBean [value1=" + value1 + ", value2=" + value2 + "]";
		}
	}
}
*/
