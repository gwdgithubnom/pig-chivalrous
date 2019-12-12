/**
 * 
 */
package org.gjgr.pig.chivalrous.thrift.builder;

import org.gjgr.pig.chivalrous.thrift.test.ICommonUserService;
import org.junit.Test;

/**
 * @author hongliuliao
 *
 * createTime:2012-12-6 下午3:24:05
 */
public class ThriftFileBuilderTest {

	private ThriftFileBuilder fileBuilder = new ThriftFileBuilder();
	
	@Test
	public void testToOutputstream() throws Exception {
		this.fileBuilder.setSourceDir("src/test/java");
		this.fileBuilder.buildToOutputStream(ICommonUserService.class, System.out);
	}
	
}
