/**
 * 
 */
package issue3;

import org.gjgr.pig.chivalrous.thrift.test.User;

/**
 * @author hongliuliao
 *
 * createTime:2012-11-23 下午1:05:44
 */
public interface IAccService {

	public User loginDo(AccDo ddvo);
	
}
