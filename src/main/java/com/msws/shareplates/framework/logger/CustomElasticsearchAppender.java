package com.msws.shareplates.framework.logger;

import java.io.IOException;

import com.internetitem.logback.elasticsearch.config.Settings;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class CustomElasticsearchAppender extends AbstractCustomElasticsearchAppender<ILoggingEvent> {
	
	private Settings currentSettings;
	
	public Settings getCurrentSettings() {
		return this.currentSettings;
	}

	public void setCurrentSettings(Settings currentSettings) {
		this.currentSettings = currentSettings;
	}
	
	public CustomElasticsearchAppender() {
    }

    public CustomElasticsearchAppender(Settings settings) {
        super(settings);
        this.currentSettings = settings;
    }

    @Override
    protected void appendInternal(ILoggingEvent eventObject) {

        String targetLogger = eventObject.getLoggerName();

        String loggerName = settings.getLoggerName();
        if (loggerName != null && loggerName.equals(targetLogger)) {
            return;
        }

        String errorLoggerName = settings.getErrorLoggerName();
        if (errorLoggerName != null && errorLoggerName.equals(targetLogger)) {
            return;
        }

        eventObject.prepareForDeferredProcessing();
        if (settings.isIncludeCallerData()) {
            eventObject.getCallerData();
        }

        publishEvent(eventObject);
    }

    protected CustomElasticsearchPublisher buildElasticsearchPublisher() throws IOException {
        return new CustomElasticsearchPublisher(getContext(), errorReporter, settings, elasticsearchProperties, headers);
    }

}
