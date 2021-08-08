package com.xy.cloud.search.aspect;


import com.alibaba.fastjson.JSON;
import com.xy.cloud.search.dao.SearchWordsSortDao;
import com.xy.cloud.search.dto.DirectoryDto;
import com.xy.cloud.search.utli.JSONUtils;
import com.xy.cloud.search.vo.SearchWordsSortVo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;

@Aspect
@Component
public class DataPointsLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(DataPointsLogAspect.class);

    @Autowired
    public SearchWordsSortDao searchWordsSortDao;

    @Pointcut("@annotation(com.xy.cloud.search.annotation.DataPointsLog)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        // 执行方法
        Object result = point.proceed();
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        //异步数据埋点
        dataPoints(point);
        return result;
    }
    /**
     * 数据埋点
     * @param joinPoint
     * @throws InterruptedException
     */
   public void dataPoints(ProceedingJoinPoint joinPoint) throws InterruptedException {
       // 接收到请求，记录请求内容
       ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
       String type = attributes.getRequest().getHeader("type");
       String params = JSONUtils.beanToJson(joinPoint.getArgs()[0]);
        DirectoryDto dto = JSON.parseObject(params,DirectoryDto.class);
        try {
            SearchWordsSortVo vo = new SearchWordsSortVo();
            vo.setSearchWordsName(dto.getName());
            vo.setUserId("");
            vo.setUpdateDate(new Date());
            vo.setLanguageType(type);
            int result = searchWordsSortDao.searchWordsSortFlag(vo);
            if(result>0){
                searchWordsSortDao.updateBySearchCount(vo);
            }else{
                vo.setCreateDate(new Date());
                searchWordsSortDao.save(vo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Before("logPointCut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        logger.info("请求地址 : " + request.getRequestURL().toString());
        logger.info("HTTP METHOD : " + request.getMethod());
        // 获取真实的ip地址
        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "."
                + joinPoint.getSignature().getName());
        logger.info("参数 : " + Arrays.toString(joinPoint.getArgs()));
    }

    @Around("logPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object ob = pjp.proceed();// ob 为方法的返回值
        logger.info("耗时 : " + (System.currentTimeMillis() - startTime));
        return ob;
    }
}
