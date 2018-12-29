package com.supermap.sample.traffic;

import com.supermap.services.components.commontypes.util.ResourceManager;
import com.supermap.services.providers.UGCMapProvider;
import org.slf4j.cal10n.LocLogger;
import org.slf4j.cal10n.LocLoggerFactory;

/**
 * 日志帮助类
 * @author myl
 *
 */
public class LogHelper {
      private static ResourceManager resource = new ResourceManager("com.supermap.services.providers.UGCMapProviderResource");
      private static LocLoggerFactory llFactoryzhCN = new LocLoggerFactory(resource);
      private static LocLogger locLogger = llFactoryzhCN.getLocLogger(UGCMapProvider.class);
      
      /**
       * 使用iserver日志功能输出日志信息
       * @param info
       */
      public static void logByIserver(String info)
      {
            //locLogger.debug(info);      
            locLogger.info(info);         
      }
      
      /**
       * 使用iserver日志功能输出日志信息
       * @param e
       */
      public static void logByIserver(Exception e)
      {
            locLogger.info(e.getMessage(),e);         
      }

}