package com.fintech.insurance.micro.support.persist.dao;

import com.fintech.insurance.commons.enums.AdStatus;
import com.fintech.insurance.components.persist.BaseEntityDaoImpl;
import com.fintech.insurance.micro.support.persist.entity.Advertisement;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class AdvertisementDaoImpl extends BaseEntityDaoImpl<Advertisement, Integer> implements AdvertisementComplexDao {
	@Override
	public Page<Advertisement> findAllAdvertisement(Integer positionId, AdStatus advStatus, int pageIndex, int pageSize) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> map = new HashMap<>();
		sb.append("from Advertisement t where displayFlag = true ");
		if (positionId != null) {
			sb.append(" and advertisementPosition.id = :positionId ");
			map.put("positionId", positionId);
		}
		if (advStatus != null) {
			Date nowTime = Calendar.getInstance().getTime();
			if (advStatus == AdStatus.NOT_STARTED) {
				sb.append(" and (t.startAt is null or t.startAt >= :nowTime) ");
				map.put("nowTime", nowTime);
			} else if (advStatus == AdStatus.FINISHED) {
				sb.append(" and (t.endAt is null or t.endAt <= :nowTime)");
				map.put("nowTime", nowTime);
			} else if (advStatus == AdStatus.UNDERWAY) {
				sb.append(" and ( t.startAt is null or t.startAt <= :beginTime ) and ( t.endAt is null or t.endAt >= :endTime ) ");
				map.put("beginTime", nowTime);
				map.put("endTime", nowTime);
			}
		}
		sb.append(" order by t.createAt desc ");
		return this.findEntityPagination("select t " + sb.toString(), "select count(t) " + sb.toString(), map, pageIndex, pageSize);
	}

    @Override
    public Advertisement advertisementExist(Integer id, String title) {
        StringBuilder sb = new StringBuilder("from Advertisement");
        Map<String, Object> map = new HashMap<String, Object>();
        if (id != null) {
            sb.append(" and id != :id ");
            map.put("id", id);
        }
        if (StringUtils.isNotBlank(title)) {
            sb.append(" and title = :title ");
            map.put("title", title);
        }
        return this.findFirstEntity(sb.toString(), map);
    }
}
