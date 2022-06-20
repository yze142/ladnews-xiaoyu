import com.heima.model.schedule.dto.Task;
import com.heima.schedule.ScheduleApplication;
import com.heima.schedule.service.Impl.TaskServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @program: heima-leadnews
 * @description:
 * @author: hello.xaioyu
 * @create: 2022-06-10 22:44
 **/
@SpringBootTest(classes = ScheduleApplication.class)
@RunWith(SpringRunner.class)
public class TaskServiceImplTest {

    @Autowired
    private TaskServiceImpl taskService;

    @Test
    public void taskTest(){
        Task task=new Task();
        task.setTaskId(Long.valueOf("1535273196499095558"));
        task.setTaskType(99);
        task.setPriority(50);
        task.setParameters("task test".getBytes());
        task.setExecuteTime(new Date().getTime());


        long l = taskService.addTask(task);


    }



    @Test
    public void taskTest2(){
        Long aLong = Long.valueOf("1535273196499095554");
        boolean b = taskService.cancelTask(aLong);

    }

    @Test
    public void taskTest3(){
        Task consumption = taskService.consumption(99, 50);
        System.out.println(consumption);
    }


    @Test
    public void taskTest4(){

        for (int i=0; i<5; i++) {
            Task task=new Task();
            task.setTaskType(99+i);
            task.setPriority(50);
            task.setParameters("task test".getBytes());
            task.setExecuteTime(new Date().getTime()+500*1);
            long l = taskService.addTask(task);
        }

    }
}
