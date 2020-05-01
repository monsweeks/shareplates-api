package com.msws.shareplates.framework.logger;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.internetitem.logback.elasticsearch.config.ElasticsearchProperties;
import com.internetitem.logback.elasticsearch.config.HttpRequestHeaders;
import com.internetitem.logback.elasticsearch.config.Property;
import com.internetitem.logback.elasticsearch.config.Settings;
import com.internetitem.logback.elasticsearch.util.AbstractPropertyAndEncoder;
import com.internetitem.logback.elasticsearch.util.ClassicPropertyAndEncoder;
import com.internetitem.logback.elasticsearch.util.ErrorReporter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Context;

public class CustomElasticsearchPublisher extends AbstractCustomElasticsearchPublisher<ILoggingEvent> {

	public CustomElasticsearchPublisher(Context context, ErrorReporter errorReporter, Settings settings,
			ElasticsearchProperties properties, HttpRequestHeaders headers) throws IOException {
		super(context, errorReporter, settings, properties, headers);
	}

	@Override
	protected AbstractPropertyAndEncoder<ILoggingEvent> buildPropertyAndEncoder(Context context, Property property) {
		return new ClassicPropertyAndEncoder(property, context);
	}
	
	@Override
	protected void serializeIndexString(JsonGenerator gen, ILoggingEvent event) throws IOException {
		gen.writeStartObject();
		gen.writeObjectFieldStart("index");
		
		if(event.getMDCPropertyMap().get("index_name") != null)
			gen.writeObjectField("_index", event.getMDCPropertyMap().get("index_name"));
		else
			gen.writeObjectField("_index", this.getIndexPattern().encode(event));

		String type = settings.getType();
		if(type != null) {
			gen.writeObjectField("_type", type);
		}
		
		gen.writeEndObject();
		gen.writeEndObject();
		
	}

	@Override
	protected void serializeCommonFields(JsonGenerator gen, ILoggingEvent event) throws IOException {
		gen.writeObjectField("@timestamp", getTimestamp(event.getTimeStamp()));

		if (settings.isRawJsonMessage()) {
			gen.writeFieldName("message");
			gen.writeRawValue(event.getFormattedMessage());
		} else {
			String formattedMessage = event.getFormattedMessage();
			if (settings.getMaxMessageSize() > 0 && formattedMessage.length() > settings.getMaxMessageSize()) {
				formattedMessage = formattedMessage.substring(0, settings.getMaxMessageSize()) + "..";
			}
			gen.writeObjectField("message", formattedMessage);
		}

		
		// add MDC data field
		if (settings.isIncludeMdc()) {
			for (Map.Entry<String, String> entry : event.getMDCPropertyMap().entrySet()) {
				gen.writeObjectField(entry.getKey(), entry.getValue());
			}
		}
	}

	

}
