package com.wakefern.global;

public final class ApplicationConstants {
	public static final String ErrorMessage = "ErrorMessage";

	public static class StringConstants {
		public static final String store = "/store";
		public static final String wakefernApplication = "wakefern application";
	}

	public static class Requests {
		public static final String unauthorizedError = "Session is not valid";

		public static class Headers {
			public static final String contentType = "Content-Type";
			public static final String Accept = "Accept";
			public static final String Authorization = "Authorization";
			public static final String userAgent = "User-Agent";
			public static final String appCode = "appCode";
			public static final String jwtToken = "JWT-Token";
			public static final String appVersion = "AppVersion";
			public static final String xSiteHost = "x-site-host";
			public static final String reservedTimeslot = "X-Reserved-Timeslot";
			public static final String xForwardedFor = "X-Forwarded-For";

			public static final String wakefernMobileVersion = "X-Wakefern-Mobile-Version";
			public static final String xApiKey = "X-Api-Key";

			public static class MIMETypes {
				public static final String generic = "application/*";
				public static final String json = "application/json";
			}
		}

		public static final String VerifyServices = "/wakefern/services/v7/verify";

		public static final String Proxy = "/proxy";
	}

	public static class Logging {
		public static final String Logging = "/logging";
		public static final String log = "log";
		public static final String LoggingAuth = "wakefern-shoprite-auth";
	}

	public static class Log {
		public static final String Log = "/log";

		public static final String Email = Log + "/email";
		public static final String Status = "/status";
		public static final String Address = "/address" + "/{addresses}";
		public static final String UpdateSetting = "/updateSettings";

		public static final String logger = Log + "/logger";
		public static final String ChangeLevel = "/level/{logLevel}";
		public static final String GetLevel = "/level";
		public static final String AppenderList = "/appender/list";

		public static final String Release = Log + "/release";
		public static final String ReleaseLevel = "/level";
		
		public static final String IsLoggable = "/loggable/{isLoggable}";
		public static final String LoggableCodes = "/loggableCodes/{codes}";
		public static final String LoggableList = "/loggableList";
	}
	
	public static class Env {
		public static final String Envs = "/envs";
	}
}
