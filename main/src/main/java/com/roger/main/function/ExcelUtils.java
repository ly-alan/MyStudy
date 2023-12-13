//package com.roger.main.function;
//
//import org.apache.commons.codec.binary.StringUtils;
//import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.util.CellUtil;
//import org.apache.poi.xssf.streaming.SXSSFWorkbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStreamWriter;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.atomic.AtomicBoolean;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.concurrent.locks.LockSupport;
//
//import kotlin.text.Charsets;
//
///**
// * @Author Roger
// * @Date 2023/12/11 16:00
// * @Description
// */
//public class ExcelUtils {
//
//    private static Integer totalIndex = 0;
//
//    private static ExecutorService executorService = Executors.newCachedThreadPool();
//
//    /**
//     * 测试写入百万条数据:支持拆分,支持百万级别以上
//     *
//     * @param singleFileSize
//     * @throws IOException
//     */
//    public static void Excel2007AboveOperate2(int singleFileSize) throws IOException {
//        SXSSFWorkbook sxssfWorkbook = null;
//        File tempFile = null;
//        int length = 1000001;
//
//        int cycles = length / singleFileSize + (length % singleFileSize == 0 ? 0 : 1);//如果按照阈值拆分有余数,则需要+1
//        int index = 0;
//        boolean flag = true;//true代表即将导出到新的文件中,false代表即将导出到旧的文件中
//        int singleIndex = 0;
//
//        for (int i = 0; i < cycles; i++) {
//            //创建临时文件&临时对象
//            tempFile = File.createTempFile("tmp_excel", ".xlsx");
//            System.out.println("临时文件所在的本地路径：" + tempFile.getCanonicalPath());
//            XSSFWorkbook workbook = new XSSFWorkbook();
//            sxssfWorkbook = new SXSSFWorkbook(workbook, 100);
//            sxssfWorkbook.createSheet("first");
//
//            for (int k = index; k <= length; k++) {   //当k循环到 指定阈值就应该跳出循环,进行分拆.
//                // 也有可能出现k到不了阈值就已经达到最后一条了,此时也需要跳出循环
//                index = k;
//                Sheet first = sxssfWorkbook.getSheetAt(0);
//                if (flag) {
//                    singleIndex = 0;
//                    flag = false;
//                }
//                Row row = first.createRow(singleIndex);
//                for (int j = 0; j < 11; j++) {
//                    if (singleIndex == 0) {
//                        // 首行
//                        row.createCell(j).setCellValue("column" + j);
//                    } else {
//                        // 数据
//                        if (j == 0) {
//                            CellUtil.createCell(row, j, String.valueOf(k));
//                        } else { CellUtil.createCell(row, j, String.valueOf(Math.random())); }
//                    }
//                }
//                if (singleIndex == singleFileSize) {
//                    break;
//                }
//                singleIndex++;
//
//            }
//
//            //写入到临时文件
//            FileOutputStream out = new FileOutputStream(tempFile);
//            sxssfWorkbook.write(out);
//            out.close();
//            flag = true;
//        }
//    }
//
//    /**
//     * 测试写入百万条数据: 支持拆分, 支持百万级别以上, 支持多sheet页 支持数据源为model类型传递
//     * <p>
//     * 思路：
//     *
//     * @param singleFileSize
//     * @throws IOException
//     */
//    public static void Excel2007AboveOperate3(int singleFileSize, List<String> components, int fetchedSize, int rowAccessWindowSize)
//            throws IOException, InvalidFormatException {
//        //组件个数
//        int nums = components.size();
//        //每个组件在每个文件中的导出的个数
//        int sheetSize = singleFileSize / nums + (singleFileSize % nums == 0 ? 0 : 1);
//        //存放数据的队列
//        LinkedList<List<Area>> queue = new LinkedList();
//        //存放每个组件的长度
//        final Map<String, Integer> lengthMap = new HashMap<>();
//        //获取每个组件长度,组件中数据量最大的值
//        sortComponents(components, lengthMap);
//        //excel导出工具
//        SXSSFWorkbook sxssfWorkbook = null;
//        //存放导出文件地址
//        List<List<String>> result = new ArrayList<>(nums);
//
//        for (int i = 0; i < components.size(); i++) {
//            //当前遍历的组件
//            String component = components.get(i);
//            //已经写出长度
//            int alreadyLen = 0;
//            ///获取列信息
//            List<String> keys = keyService(component);
//            //设置当前组件导出文件地址
//            result.add(i, new ArrayList<>());
//            AtomicInteger fetchedNum = new AtomicInteger();
//            //启动线程获取数据
//            Thread vice = new Thread(() -> {
//                while (true) {
//                    if (fetchedNum.get() != 0 && fetchedNum.get() >= lengthMap.get(component)) {
//                        return;
//                    }
//                    //获取数据
//                    DataModel<Area> data = dataService(component, fetchedNum.get(), fetchedSize);
//                    //存放结果
//                    queue.addLast(data.getResult());
//                    fetchedNum.addAndGet(fetchedSize);
//                }
//            }, "vice");
//
//            vice.start();
//
//            File tempFile = null;
//            Integer length = lengthMap.get(component);
//            int cycles = length / sheetSize + (length % sheetSize == 0 ? 0 : 1);//如果按照阈值拆分有余数,则需要+1
//            boolean flag = true;//true代表即将导出到新的文件中,false代表即将导出到旧的文件中
//            int singleIndex = 0;
//
//            for (int q = 0; q < cycles; q++) {
//                String tempFileStr = null;
//                try {
//                    tempFileStr = result.get(0).get(q);
//                } catch (IndexOutOfBoundsException e) {
//
//                }
//
//                if (StringUtils.isEmpty(tempFileStr)) {
//                    //创建临时文件&临时对象
//                    tempFile = File.createTempFile("tmp_excel", ".xlsx");
//                    //System.out.println("临时文件所在的本地路径：" + tempFile.getCanonicalPath());
//                    XSSFWorkbook workbook = new XSSFWorkbook();
//                    sxssfWorkbook = new SXSSFWorkbook(workbook, rowAccessWindowSize);
//                } else {
//                    //创建临时文件&临时对象
//                    tempFile = new File(tempFileStr);
//                    //System.out.println("临时文件所在的本地路径：" + tempFile.getCanonicalPath());
//                    FileInputStream in = new FileInputStream(tempFile);
//                    XSSFWorkbook workbook = new XSSFWorkbook(in);
//                    in.close();
//                    sxssfWorkbook = new SXSSFWorkbook(workbook, rowAccessWindowSize);
//                }
//                sxssfWorkbook.createSheet();
//                sxssfWorkbook.setSheetName(i, component);
//                //                System.out.println(sxssfWorkbook.getSheetAt(0));
//
//                while (true) {
//
//                    if (lengthMap.get(component) != null && lengthMap.get(component) <= alreadyLen) {//退出条件:1 写完了 2 达到阈值了
//                        break;
//                    }
//                    if (singleIndex >= sheetSize) {
//                        break;
//                    }
//
//                    List<Area> first = queue.poll();//从队列中拿结果
//                    while (first == null) {
//                        LockSupport.parkNanos(10000000);
//                        first = queue.poll();
//
//                        if (lengthMap.get(component) != null && lengthMap.get(component) <= alreadyLen) {//说明另一个线程还没有获取结果，休眠片刻
//                            break;
//                        }
//                        if (singleIndex > sheetSize) {
//                            break;
//                        }
//                    }
//                    if (first == null) {
//                        break;
//                    }
//
//                    for (Area area : first) {//拿到数据
//                        Sheet firstSheet = sxssfWorkbook.getSheetAt(i);
//
//                        Row row = firstSheet.createRow(singleIndex);
//                        if (singleIndex == 0) {// 首行
//                            for (int j = 0; j < keys.size(); j++) {
//
//                                row.createCell(j).setCellValue(keys.get(j));
//                            }
//                            row = firstSheet.createRow(++singleIndex);
//                        }
//
//                        for (int j = 0; j < keys.size(); j++) {// 数据
//
//                            CellUtil.createCell(row, j, getFieldValueByName(keys.get(j), area).toString());
//                        }
//                        singleIndex++;
//                        alreadyLen++;
//                    }
//
//                }
//
//                //写入到临时文件
//                FileOutputStream out = new FileOutputStream(tempFile);
//                sxssfWorkbook.write(out);
//                out.flush();
//                out.close();
//                result.get(i).add(q, tempFile.getAbsolutePath());
//                singleIndex = 0;
//                if (lengthMap.get(component) <= alreadyLen) {
//                    break;
//                }
//
//            }
//
//        }
//
//        //结束，上传文件
//        //打印文件
//        /*for (int i = 0; i < result.size(); i++) {
//            System.out.println(components.get(i)+"export over ...");
//            System.out.println(result.get(i).size());
//            for (String string : result.get(i)) {
//                System.out.print(string + ";");
//            }
//            System.out.println();
//
//        }*/
//
//    }
//
//    /**
//     * 测试写入百万条数据: 支持拆分, 支持百万级别以上, 支持多sheet页 支持数据源为model类型传递 优化版:240w数据56s 思路：
//     *
//     * @param singleFileSize
//     * @throws IOException
//     */
//    public static List<String> Excel2007AboveOperate4(int singleFileSize, List<String> components, int fetchedSize, int rowAccessWindowSize)
//            throws IOException, InvalidFormatException {
//        //组件个数
//        int nums = components.size();
//        //每个组件在每个文件中的导出的个数
//        int sheetSize = singleFileSize / nums + (singleFileSize % nums == 0 ? 0 : 1);
//        //存放数据的队列
//        LinkedList<List<Area>> queue = new LinkedList();
//        //存放每个组件的长度
//        final Map<String, Integer> lengthMap = new HashMap<>();
//        //获取每个组件长度,组件中数据量最大的值
//        sortComponents(components, lengthMap);
//        //excel导出工具
//        SXSSFWorkbook sxssfWorkbook = null;
//        //存放导出文件地址
//        List<String> result = new ArrayList<>(nums);
//        //存放组件对应的tempFile和workbook
//        int oneCycles = lengthMap.get(components.get(0)) / sheetSize + (lengthMap.get(components.get(0)) % sheetSize == 0 ? 0 : 1);
//        List<SXSSFWorkbook> workbooks = new ArrayList<>(oneCycles);
//        for (int i = 0; i < oneCycles; i++) {
//            XSSFWorkbook workbook = new XSSFWorkbook();
//            sxssfWorkbook = new SXSSFWorkbook(workbook, rowAccessWindowSize);
//            workbooks.add(sxssfWorkbook);
//        }
//
//        for (int i = 0; i < components.size(); i++) {
//            //当前遍历的组件
//            String component = components.get(i);
//            //已经写出长度
//            int alreadyLen = 0;
//            ///获取列信息
//            List<String> keys = keyService(component);
//            AtomicInteger fetchedNum = new AtomicInteger();
//            //启动线程获取数据
//            Thread vice = new Thread(() -> {
//                while (true) {
//                    if (fetchedNum.get() != 0 && fetchedNum.get() >= lengthMap.get(component)) {
//                        return;
//                    }
//                    //获取数据
//                    DataModel<Area> data = dataService(component, fetchedNum.get(), fetchedSize);
//                    //存放结果
//                    queue.addLast(data.getResult());
//                    fetchedNum.addAndGet(fetchedSize);
//                }
//            }, "vice");
//
//            vice.start();
//
//            File tempFile = null;
//            Integer length = lengthMap.get(component);
//            int cycles = length / sheetSize + (length % sheetSize == 0 ? 0 : 1);//如果按照阈值拆分有余数,则需要+1
//            boolean flag = true;//true代表即将导出到新的文件中,false代表即将导出到旧的文件中
//            int singleIndex = 0;
//
//            for (int q = 0; q < cycles; q++) {
//
//                sxssfWorkbook = workbooks.get(q);
//                sxssfWorkbook.createSheet();
//                sxssfWorkbook.setSheetName(i, component);
//                //                System.out.println(sxssfWorkbook.getSheetAt(0));
//
//                while (true) {
//
//                    if (lengthMap.get(component) != null && lengthMap.get(component) <= alreadyLen) {//退出条件:1 写完了 2 达到阈值了
//                        break;
//                    }
//                    if (singleIndex >= sheetSize) {
//                        break;
//                    }
//
//                    List<Area> first = queue.poll();//从队列中拿结果
//                    while (first == null) {
//                        LockSupport.parkNanos(10000000);
//                        first = queue.poll();
//
//                        if (lengthMap.get(component) != null && lengthMap.get(component) <= alreadyLen) {//说明另一个线程还没有获取结果，休眠片刻
//                            break;
//                        }
//                        if (singleIndex > sheetSize) {
//                            break;
//                        }
//                    }
//                    if (first == null) {
//                        break;
//                    }
//
//                    for (Area area : first) {//拿到数据
//                        Sheet firstSheet = sxssfWorkbook.getSheetAt(i);
//
//                        Row row = firstSheet.createRow(singleIndex);
//                        if (singleIndex == 0) {// 首行
//                            for (int j = 0; j < keys.size(); j++) {
//
//                                row.createCell(j).setCellValue(keys.get(j));
//                            }
//                            row = firstSheet.createRow(++singleIndex);
//                        }
//
//                        for (int j = 0; j < keys.size(); j++) {// 数据
//
//                            CellUtil.createCell(row, j, getFieldValueByName(keys.get(j), area).toString());
//                        }
//                        singleIndex++;
//                        alreadyLen++;
//                    }
//
//                }
//
//                singleIndex = 0;
//                if (lengthMap.get(component) <= alreadyLen) {
//                    break;
//                }
//
//            }
//
//        }
//
//        //输出到文件
//        for (int i = 0; i < workbooks.size(); i++) {
//            File tempFile = File.createTempFile("tmp_excel", ".xlsx");
//            FileOutputStream out = new FileOutputStream(tempFile);
//            workbooks.get(i).write(out);
//            out.flush();
//            out.close();
//            // dispose of temporary files backing this workbook on disk
//            workbooks.get(i).dispose();
//            result.add(i, tempFile.getAbsolutePath());
//        }
//
//        return result;
//
//        //结束，上传文件
//        //打印文件
//        /*for (int i = 0; i < result.size(); i++) {
//            System.out.println(components.get(i)+"export over ...");
//            System.out.println(result.get(i).size());
//            for (String string : result.get(i)) {
//                System.out.print(string + ";");
//            }
//            System.out.println();
//
//        }*/
//
//    }
//
//    /**
//     * 测试写入百万条数据: 支持拆分, 支持百万级别以上, 支持多sheet页 支持数据源为model类型传递 优化版:240w数据56s 方法拆分 思路：
//     *
//     * @param singleFileSize
//     * @throws IOException
//     */
//    public static List<String> Excel2007AboveOperate5(int singleFileSize, List<String> components, int fetchedSize, int rowAccessWindowSize)
//            throws Exception {
//        //组件个数
//        int nums = components.size();
//        //存放数据的队列
//        LinkedList<List<Area>> queue = new LinkedList();
//        //存放每个组件的长度
//        final Map<String, Integer> lengthMap = new HashMap<>();
//        //excel导出工具
//        SXSSFWorkbook sxssfWorkbook = null;
//        //存放导出文件地址
//        List<String> result = new ArrayList<>(nums);
//
//        //计算每个组件在每个文件中的导出的个数
//        int sheetSize = singleFileSize / nums + (singleFileSize % nums == 0 ? 0 : 1);
//        //获取每个组件的长度,并对components进行排序,长度最大的component的索引一定是1
//        sortComponents(components, lengthMap);
//        //计算最终需要拆分多少excel文件
//        int oneCycles = lengthMap.get(components.get(0)) / sheetSize + (lengthMap.get(components.get(0)) % sheetSize == 0 ? 0 : 1);
//
//        //1 统一初始化workbooks
//        List<SXSSFWorkbook> workbooks = new ArrayList<>(oneCycles);
//        for (int i = 0; i < oneCycles; i++) {
//            XSSFWorkbook workbook = new XSSFWorkbook();
//            sxssfWorkbook = new SXSSFWorkbook(workbook, rowAccessWindowSize);
//            workbooks.add(sxssfWorkbook);
//        }
//
//        //2 遍历components,一个个导出组件到对应的workbook中
//        exportComponent(components,lengthMap,fetchedSize,queue,sheetSize, workbooks);
//
//        //3 写出到指定文件中
//        writeToFile(workbooks,result);
//        //3 上传文件
//        //4 删除临时文件
//        return result;
//
//        //结束，上传文件
//
//    }
//
//    //不需要对线程阻塞做最大时间限制,因为副线程调用rpc超时会自动抛异常,主线程因此也会抛出异常
//    private static void exportComponent(List<String> components, final Map<String, Integer> lengthMap, int fetchedSize,
//                                        LinkedList<List<Area>> queue, int sheetSize,
//                                        List<SXSSFWorkbook> workbooks)
//
//            throws Exception {
//
//        //错误信号,用于检测主副线程是否发生错误.
//        AtomicBoolean isError = new AtomicBoolean(false);
//        try {
//
//            //遍历components,一个组件导出完成后,再对另一个组件导出
//            for (int i = 0; i < components.size(); i++) {
//                //当前遍历的组件
//                String component = components.get(i);
//                //已经写出长度
//                int alreadyLen = 0;
//                ///获取列信息
//                List<String> keys = keyService(component);
//                //已从远程捞取的数量
//                AtomicInteger fetchedNum = new AtomicInteger(0);
//                //启动线程获取数据
//                executorService.submit( () -> {
//
//                    try {
//                        while (true) {
//                            if ( fetchedNum.get() >= lengthMap.get(component)  ) {
//                                return;
//                            }
//                            //对isError做断言
//                            Assert.isTrue(!isError.get(),"发生异常");
//                            //获取数据
//                            DataModel<Area> data = dataService(component, fetchedNum.get(), fetchedSize);
//                            //存放结果
//                            queue.addLast(data.getResult());
//                            fetchedNum.addAndGet(fetchedSize);
//                        }
//
//                    }catch (Exception e){
//                        //打印错误日志
//                        //亮出信号灯,让主线程停止
//                        isError.set(true);
//                        throw e;
//                    }
//
//                });
//
//                Integer length = lengthMap.get(component);
//                int cycles = length / sheetSize + (length % sheetSize == 0 ? 0 : 1);//如果按照阈值拆分有余数,则需要+1
//                int singleIndex = 0;
//
//                for (int q = 0; q < cycles; q++) {
//
//                    SXSSFWorkbook sxssfWorkbook = workbooks.get(q);
//                    sxssfWorkbook.createSheet();
//                    sxssfWorkbook.setSheetName(i, component);
//
//                    while (true) {
//
//                        if (  alreadyLen >= lengthMap.get(component) || singleIndex >= sheetSize  ) {//退出条件:1 写完了 2 达到阈值了 3 发生错误了
//                            break;
//                        }
//
//
//
//                        //从队列中拿结果
//                        List<Area> first ;
//                        while ((first = queue.poll()) == null ) {//如果等于null,
//
//                            //对isError做断言
//                            Assert.isTrue(!isError.get(),"发生异常");
//
//                            //说明另一个线程还没有获取结果，休眠片刻
//                            LockSupport.parkNanos(10000000);
//
//                        }
//                        //对isError做断言
//                        Assert.isTrue(!isError.get(),"发生异常");
//
//                        //拿到数据
//                        for (Area area : first) {
//                            Sheet firstSheet = sxssfWorkbook.getSheetAt(i);
//                            Row row = firstSheet.createRow(singleIndex);
//
//                            // 首行
//                            if (singleIndex == 0) {
//                                for (int j = 0; j < keys.size(); j++) {
//
//                                    row.createCell(j).setCellValue(keys.get(j));
//                                }
//                                row = firstSheet.createRow(++singleIndex);
//                            }
//                            //填充数据
//                            for (int j = 0; j < keys.size(); j++) {
//                                //使用cglib的BeanMap将bean转成Map,性能较低
//                                CellUtil.createCell(row, j, getFieldValueByName(keys.get(j), area).toString());
//                            }
//                            singleIndex++;
//                            alreadyLen++;
//                        }
//
//
//                    }
//
//                    singleIndex = 0;
//                    if (lengthMap.get(component) <= alreadyLen) {
//                        break;
//                    }
//
//                }
//
//            }
//
//
//        }catch (Exception e){
//            isError.set(true);
//            throw e;
//        }
//
//    }
//
//    public static void writeToFile(List<SXSSFWorkbook> workbooks,List<String> result) throws IOException {
//
//        FileOutputStream out = null;
//        try {
//
//
//            for (int i = 0; i < workbooks.size(); i++) {
//                File tempFile = File.createTempFile("tmp_excel", ".xlsx");
//                out = new FileOutputStream(tempFile);
//                workbooks.get(i).write(out);
//                out.flush();
//                out.close();
//                // dispose of temporary files backing this workbook on disk
//                workbooks.get(i).dispose();
//                result.add(i, tempFile.getAbsolutePath());
//            }
//
//
//        } catch (IOException e) {
//            //log
//            throw e;
//        }finally {
//            if(out != null){
//                out.close();
//            }
//        }
//
//    }
//
//    //对components 逆序排序
//    private static void sortComponents(List<String> components, Map<String, Integer> lengthMap) {
//        components.forEach((component) -> lengthMap.put(component, dataService(component, 0, 1).getTotalNum()));
//        components.sort((o1,o2) -> lengthMap.get(o2) - lengthMap.get(o1));
//
//    }
//
//    /**
//     * 支持百万级别 支持拆分 支持数据源为model类型传递
//     * <p>
//     * 思路：
//     *
//     * @param singleFileSize 单个文件的大小
//     * @param components     多个组件名
//     */
//    public static void exportCsv(int singleFileSize, List<String> components, int fetchedSize) throws Exception {
//        //错误信号,用于检测主副线程是否发生错误.
//        AtomicBoolean isError = new AtomicBoolean(false);
//
//        try {
//
//            //组件个数
//            int nums = components.size();
//            //存放数据的队列
//            LinkedList<List<Area>> queue = new LinkedList();
//            //存放导出文件地址
//            List<List<String>> result = new ArrayList<>(nums);
//            //存放每个组件的长度
//            final Map<String, Integer> lengthMap = new HashMap<>(nums);
//            //get the length of every component
//            components.forEach((component) -> lengthMap.put(component, dataService(component, 0, 1).getTotalNum()));
//
//            for (int i = 0; i < components.size(); i++) {
//                //当前遍历的组件
//                String component = components.get(i);
//                //已经写出长度
//                int alreadyLen = 0;
//                ///获取列信息
//                List<String> keys = keyService(component);
//                String[] keysArray = new String[keys.size()];
//                keysArray = keys.toArray(keysArray);
//                //set the header of  csv file
//                CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader(keysArray);
//                //设置当前组件导出文件地址
//                result.add(i, new ArrayList<>());
//                //已经从远程获取的数据量
//                AtomicInteger fetchedNum = new AtomicInteger(0);
//                //启动线程获取数据
//                executorService.execute(() -> {
//                    try {
//
//                        while (fetchedNum.get() < lengthMap.get(component)) {
//                            //对isError做断言
//                            Assert.isTrue(!isError.get(),"发生异常");
//                            //获取数据
//                            DataModel<Area> data = dataService(component, fetchedNum.get(), fetchedSize);
//                            //存放结果
//                            queue.addLast(data.getResult());
//                            fetchedNum.addAndGet(fetchedSize);
//
//                        }
//
//
//                    }catch (Exception e){
//                        isError.set(true);
//                        throw e;
//                    }
//
//                });
//
//
//                CSVPrinter csvPrinter = null;
//                OutputStreamWriter bufferedOut = null;
//                try {
//
//                    int length = lengthMap.get(component);
//                    int cycles = length / singleFileSize + (length % singleFileSize == 0 ? 0 : 1);//如果按照阈值拆分有余数,则需要+1
//                    int singleIndex = 0;//记录写入的个数,在每次循环时将更新为0
//
//                    for (int q = 0; q < cycles; q++) {
//                        //创建临时文件&临时对象
//                        File tempFile = File.createTempFile("tmp_" + component+"_", ".csv");
//                        bufferedOut = new OutputStreamWriter(new FileOutputStream(tempFile), Charsets.UTF_8);
//                        csvPrinter = new CSVPrinter(bufferedOut, csvFormat);
//
//                        while (singleIndex < singleFileSize && lengthMap.get(component) > alreadyLen) {//退出条件:1 写完了 2 达到阈值了
//
//                            List<Area> first ;//从队列中拿结果
//                            while ((first = queue.poll()) == null) {
//                                //对isError做断言
//                                Assert.isTrue(!isError.get(),"发生异常");
//                                LockSupport.parkNanos(10000000);
//                            }
//
//                            //对isError做断言
//                            Assert.isTrue(!isError.get(),"发生异常");
//
//                            Object[] value = new Object[keys.size()];
//                            for (Area area : first) {//拿到数据
//
//                                for (int j = 0; j < keys.size(); j++) {// 数据
//                                    Object getValue = getFieldValueByName(keys.get(j), area);
//                                    value[j] = Optional.ofNullable(getValue).orElse("");
//                                }
//                                csvPrinter.printRecord(value);
//                                singleIndex++;
//                                alreadyLen++;
//                            }
//                        }
//
//                        //写入到临时文件
//                        csvPrinter.flush();
//                        csvPrinter.close();
//                        result.get(i).add(q, tempFile.getAbsolutePath());
//                        singleIndex = 0;
//
//                    }
//
//
//                }catch (Exception e){
//                    isError.set(true);
//                    throw e;
//                }finally {
//                    if(csvPrinter != null){
//                        csvPrinter.close();
//                    }
//                    if(bufferedOut != null){
//                        bufferedOut.close();
//                    }
//                }
//
//            }
//
//            //结束，上传文件
//            //打印文件
//
//
//        }catch (Exception e){
//            isError.set(true);
//            throw e;
//        }
//
//
//    }
//
//
//
//    private static DataModel<Area> dataService(String component, int fromIndex, int size) {
//
//        //模拟网络传输的迟钝性.延迟100ms
//        /*try {
//            Thread.sleep(300L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }*/
//        int totalNum = 0;
//        if (component.equals("aaa")) {
//
//            totalNum = 800000;
//
//        } else if (component.equals("bbb")) {
//
//            totalNum = 600000;
//
//        } else {
//
//            totalNum = 1000000;
//        }
//
//        List<Area> result = new ArrayList<>(size);
//        for (int i = fromIndex; i < (fromIndex + size < totalNum ? fromIndex + size : totalNum); i++) {
//
//            Area area = new Area();
//            area.setCreateTime(new Date());
//            area.setLastEditTime(new Date());
//            area.setAreaId(totalIndex++);
//            area.setAreaName("水果");
//            area.setPriority(i % 3);
//
//            result.add(area);
//        }
//
//        DataModel dataModel = new DataModel();
//        dataModel.setResult(result);
//        dataModel.setTotalNum(totalNum);
//
//        return dataModel;
//    }
//
//    private static List<String> keyService(String component) {
//
//        List<String> result = new ArrayList<>(5);
//        result.add("areaId");
//        result.add("areaName");
//        result.add("priority");
//        result.add("createTime");
//        result.add("lastEditTime");
//
//        return result;
//    }
//
//    public static void main(String[] args) throws Exception {
//        ArrayList components = new ArrayList(3);
//        components.add("aaa");
//        components.add("bbb");
//        components.add("ccc");
//        long start = System.currentTimeMillis();
//        exportCsv(400000,components,1000);
//        //List<String> list = Excel2007AboveOperate5(400000, components, 6000, 1000);
//        long end = System.currentTimeMillis();
//        System.out.println("time:" + (end - start) + "ms");
//        //for (String o : list) {
//        //    System.out.println(o);
//        //}
//    }
//
//    /* 根据属性名获取属性值
//     * */
//    private static Object getFieldValueByName(String fieldName, Object o) {
//        try {
//            String firstLetter = fieldName.substring(0, 1).toUpperCase();
//            String getter = "get" + firstLetter + fieldName.substring(1);
//            Method method = o.getClass().getMethod(getter, new Class[] {});
//            Object value = method.invoke(o, new Object[] {});
//            return value;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//}
