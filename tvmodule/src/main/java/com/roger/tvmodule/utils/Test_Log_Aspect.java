package com.roger.tvmodule.utils;

import com.roger.c_annotations.Test_Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @Author Roger
 * @Date 2023/1/3 16:42
 * @Description
 */
@Aspect
//@Component
//@Slf4j
public class Test_Log_Aspect {

    /**
     * 切点，可以对类，方法等进行切入
     * 对有Test_Log标记注解的切入
     */
    @Pointcut("@annotation(com.roger.c_annotations.Test_Log)")
    public void printLog() {

    }

    @Before("printLog()")
    public void doBefore(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Test_Log annotation = method.getAnnotation(Test_Log.class);
        if (annotation == null) {
            System.out.println(String.format("annotation start null"));
            return;
        }

        String methodName = method.getDeclaringClass().getSimpleName() + "." + method.getName();

        System.out.println(String.format("start ", methodName));
    }

    @Around("printLog()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        long beginTime = System.currentTimeMillis();
        joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long dx = endTime - beginTime;
        System.out.println("耗时：" + dx + "ms");
    }

//    @Around("printLog()")
//    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
////        Logger log = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
////        //获取注解的方法
//        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
//        Test_Log annotation = method.getAnnotation(Test_Log.class);
//        if (null == annotation) {
//            return joinPoint.getArgs();
//        }
//        //注解添加的tag
//        String tag = annotation.tag();
//        long startTime = System.currentTimeMillis();
//        System.out.println(String.format("[环绕日志]>>>>>>>>>>>>>>>>>>>>>>>>>方法%s开始执行,开始时间%s", tag, startTime));
////        log.info("[环绕日志]>>>>>>>>>>>>>>>>>>>>>>>>>方法[{}]开始执行,开始时间{}", tag, startTime);
////        HttpServletRequest request = getRequest();
////        log.info("[环绕日志]>>>>>>>>>>>>>>>>>>>>>>>>>请求方法路径为:{}", request.getRequestURL());
//        StringBuilder params = new StringBuilder();
//        //参数值
//        Object[] argValues = joinPoint.getArgs();
//        //参数名称
//        String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
//        if (argValues != null) {
//            for (int i = 0; i < argValues.length; i++) {
//                params.append(argNames[i]).append(":").append(argValues[i]);
//            }
//        }
////        log.info("[环绕日志]>>>>>>>>>>>>>>>>>>>>>>>>>请求参数为:[{}]", params + "");
//        Object proceed = joinPoint.proceed();
//////        log.info("[环绕日志]>>>>>>>>>>>>>>>>>>>>>>>>>响应参数为: [{}]", JSON.toJSON(proceed));
////        log.info("[环绕日志]>>>>>>>>>>>>>>>>>>>>>>>>>执行完毕耗时 :{}", (System.currentTimeMillis() - startTime));
//        return proceed;
//    }

//    /**
//     * 异常日志打印
//     *
//     * @param joinPoint
//     * @param e
//     */
//    @AfterThrowing(pointcut = "printLog()", throwing = "e")
//    public void throwIng(JoinPoint joinPoint, Throwable e) {
//        log.info("[异常日志]>>>>>>>>>>>>>>>>>>>>>>>>>开始进行记录");
//        String stackTrace = stackTrace(e);
//        HttpServletRequest request = getRequest();
//        String ipAddr = IpUtils.getIpAddr(request);
//        log.info("[异常日志]>>>>>>>>>>>>>>>>>>>>>>>>>当前请求的Ip地址为:{}", ipAddr);
//        log.info("[异常日志]>>>>>>>>>>>>>>>>>>>>>>>>>错误信息为:{}", stackTrace);
//        log.info("[异常日志]>>>>>>>>>>>>>>>>>>>>>>>>>异常日志记录完毕");
//    }

    /**
     * 堆栈异常获取
     *
     * @param throwable
     * @return
     */
    private static String stackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }

}
