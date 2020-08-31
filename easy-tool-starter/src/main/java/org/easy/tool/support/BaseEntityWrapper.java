
package org.easy.tool.support;



import org.easy.tool.web.PR;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 视图包装基类
 *
 * @author Chill
 */
public abstract class BaseEntityWrapper<E, V> {

	/**
	 * 单个实体类包装
	 * @param entity
	 * @return
	 */
	public abstract V entityVO(E entity);

	/**
	 * 实体类集合包装
	 * @param list
	 * @return
	 */
	public List<V> listVO(List<E> list) {
		return list.stream().map(this::entityVO).collect(Collectors.toList());
	}

	/**
	 * 分页实体类集合包装
	 * @return
	 */
	public PR<List<V>> pageVO(List<E> list, Query query) {
		return pageVO(list,query.getTotal(),query.getSize());
	}

	/**
	 * 分页实体类集合包装
	 * @return
	 */
	public PR<List<V>> pageVO(List<E> list,int total,int size) {
		List<V> listVO=this.listVO(list);
		PR<List<V>> pageResult=new PR<List<V>>();
		pageResult.setData(listVO);
		pageResult.setTotal(total);
		pageResult.setSize(size);
		return pageResult;
	}

}
