package com.codeathon.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cabin4j.suite.platform.constants.Constants;
import com.cabin4j.suite.platform.dao.GenericDao;
import com.cabin4j.suite.platform.data.SearchParams;
import com.cabin4j.suite.platform.data.SearchResult;
import com.cabin4j.suite.platform.exceptions.Cabin4jException;
import com.cabin4j.suite.platform.utility.C4JAssert;
import com.codeathon.entities.NumberSeries;
import com.codeathon.service.NumberSeriesService;

@Service("numberSeriesService")
public class NumberSeriesServiceImpl implements NumberSeriesService {

	private static final char PAD_CHAR = '0';	
	
	@Autowired
    private GenericDao genericDao;

	@Override
	public String getNext(String code) {
		C4JAssert.isNotBlank(code, "Code must not be blank.");
		List<SearchParams> searchParams = new ArrayList<>();
		searchParams.add(new SearchParams("code", Constants.QueryComparator.EQUALS, code.trim()));
		try {
			SearchResult<NumberSeries> result = genericDao.loadAll(searchParams, NumberSeries.class, -1, -1);
			if(null != result && CollectionUtils.isNotEmpty(result.getResults())) {
				NumberSeries series = result.getResults().get(0);
				String next = StringUtils.isNotBlank(series.getPrefix()) ? series.getPrefix() : StringUtils.EMPTY;
				Long value = null != series.getValue() ? series.getValue() : 0l;
				if(null != series.getDigits()) {
					next = next + StringUtils.leftPad(String.valueOf(value), series.getDigits(), PAD_CHAR);
				} else {
					next = next + String.valueOf(value);
				}
				value = value + 1;
				series.setValue(value);
				genericDao.saveOrUpdateAndRefresh(series);
				return next;
			} else {
				throw new Cabin4jException("Unable to find number series entry for code: " + code);
			}
		} catch (Cabin4jException e) {
			throw e;
		} catch (Exception e) {
			throw new Cabin4jException("Exception occurred while getting next series value for: " + code);
		}
	}
}