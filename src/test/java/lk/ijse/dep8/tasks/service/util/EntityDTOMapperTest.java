package lk.ijse.dep8.tasks.service.util;

import lk.ijse.dep8.tasks.dto.TaskDTO;
import lk.ijse.dep8.tasks.dto.TaskListDTO;
import lk.ijse.dep8.tasks.dto.UserDTO;
import lk.ijse.dep8.tasks.entity.Task;
import lk.ijse.dep8.tasks.entity.TaskList;
import lk.ijse.dep8.tasks.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityDTOMapperTest {

    @Test
    void getUserDTO() {
        //Given
        User user = new User("U001", "uppa@gmail.com", "adcd", "Thilina Upendra", "UppaPic");
        UserDTO userDTO = EntityDTOMapper.getUserDTO(user);

        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getEmail(), userDTO.getEmail());
        assertEquals(user.getPassword(), userDTO.getPassword());
        assertEquals(user.getFullName(), userDTO.getName());
        assertEquals(user.getProfilePic(), userDTO.getPicture());
    }

    @Test
    void getTaskListsDTO() {
        TaskList taskList = new TaskList(1, "Uppa", "U001");
        TaskListDTO taskListDTO = EntityDTOMapper.getTaskListDTO(taskList);
        assertEquals(taskList.getId(), taskListDTO.getId());
        assertEquals(taskList.getName(), taskListDTO.getTitle());
        assertEquals(taskList.getUserId(), taskListDTO.getUserId());
    }

    @Test
    void getTaskDTO() {
        Task task = new Task(1, "Title One", "Details One", 1, Task.Status.needsAction, 1);
        TaskDTO taskDTO = EntityDTOMapper.getTaskDTO(task);
        assertEquals(task.getId(), taskDTO.getId());
        assertEquals(task.getTitle(), taskDTO.getTitle());
        assertEquals(task.getDetails(), taskDTO.getNotes());
        assertEquals(task.getPosition(), taskDTO.getPosition());
        assertEquals(task.getStatus().toString(), taskDTO.getStatus());
        assertEquals(task.getTaskListId(), taskDTO.getTaskListId());
    }

    @Test
    void getUser() {
        UserDTO userDTO = new UserDTO("U001", "Upendra", "uppa@gmail.com", "abc", "Pic");
        User user = EntityDTOMapper.getUser(userDTO);
        assertEquals(userDTO.getId(), user.getId());
        assertEquals(userDTO.getEmail(), user.getEmail());
        assertEquals(userDTO.getPassword(), user.getPassword());
        assertEquals(userDTO.getName(), user.getFullName());
        assertEquals(userDTO.getPicture(), user.getProfilePic());
    }

    @Test
    void getTaskList() {
        TaskListDTO taskListDTO = new TaskListDTO(1, "Title one", "U001");
        TaskList taskList = EntityDTOMapper.getTaskList(taskListDTO);
        assertEquals(taskListDTO.getId(), taskList.getId());
        assertEquals(taskListDTO.getTitle(), taskList.getName());
        assertEquals(taskListDTO.getUserId(), taskList.getUserId());
    }

    @Test
    void getTask() {
        TaskDTO taskDTO = new TaskDTO(1, "Title one", 1, "Note one", "needsAction", 1);
        Task task = EntityDTOMapper.getTask(taskDTO);
        assertEquals(taskDTO.getId(), task.getId());
        assertEquals(taskDTO.getTitle(), task.getTitle());
        assertEquals(taskDTO.getNotes(), task.getDetails());
        assertEquals(taskDTO.getPosition(), task.getPosition());
        assertEquals(taskDTO.getStatus(), task.getStatus().toString());
        assertEquals(taskDTO.getTaskListId(), task.getTaskListId());
    }
}