package com.ruidev.framework.freemarker.directive;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ruidev.framework.util.RequestContext;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component("pg")
public class PaginationDirective implements TemplateDirectiveModel {

	@SuppressWarnings("rawtypes")
	public void execute(Environment env, Map arg1, TemplateModel[] arg2, TemplateDirectiveBody body) throws TemplateException, IOException {
		Writer writer = env.getOut();
		int curP = RequestContext.getIndex();
		long totalR = RequestContext.getTotal();
		int everyP = RequestContext.getSize();
		double pagesCount = Math.ceil(totalR / (everyP * 1.0));
		int totalP = totalR == 0 ? 0 : ((Double)pagesCount).intValue();
		StringBuffer buffer = new StringBuffer("<input type='hidden' class='every_page' name='page.everyPage' value='").append(everyP).append("'/>");
		buffer.append("<input type='hidden' class='total_records' name='page.totalRecords' value='").append(totalR).append("'/>");
		buffer.append("<input type='hidden' class='current_page' name='page.currentPage' value='").append(curP).append("'/>");
		buffer.append("<input type='hidden' class='total_page' name='page.totalPage' value='").append(totalP).append("'/>");
		writer.write(buffer.toString());
	}

}
