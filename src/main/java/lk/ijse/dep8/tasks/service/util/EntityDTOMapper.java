package lk.ijse.dep8.tasks.service.util;

import lk.ijse.dep8.tasks.dto.TaskDTO;
import lk.ijse.dep8.tasks.dto.TaskListDTO;
import lk.ijse.dep8.tasks.dto.UserDTO;
import lk.ijse.dep8.tasks.entity.Task;
import lk.ijse.dep8.tasks.entity.TaskList;
import lk.ijse.dep8.tasks.entity.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

public interface EntityDTOMapper {

    public static UserDTO getUserDTO(User user){
        ModelMapper mapper = new ModelMapper();
        TypeMap<User, UserDTO> typeMap = mapper.typeMap(User.class, UserDTO.class);
        typeMap.addMapping(s->s.getProfilePic(), ((d, o) -> d.setPicture((String) o)));
        return mapper.map(user, UserDTO.class);
    }
    public static TaskListDTO getTaskListDTO(TaskList taskList){
        ModelMapper mapper = new ModelMapper();
        TypeMap<TaskList, TaskListDTO> typeMap = mapper.typeMap(TaskList.class, TaskListDTO.class);
        typeMap.addMapping(s->s.getName(), (t, o) -> t.setTitle((String) o));
        return mapper.map(taskList, TaskListDTO.class);
    }
    public static TaskDTO getTaskDTO(Task task){
        ModelMapper mapper = new ModelMapper();
        TypeMap<Task, TaskDTO> typeMap = mapper.typeMap(Task.class, TaskDTO.class);
        typeMap.addMapping(s->s.getDetails(), (t, o) -> t.setNotes((String) o));
        typeMap.addMapping(s->s.getStatus(), (taskDTO, o) -> taskDTO.setStatus((String)  o));
        return mapper.map(task , TaskDTO.class);
    }

    public static User getUser(UserDTO userDTO){
        ModelMapper mapper = new ModelMapper();
        TypeMap<UserDTO, User> typeMap = mapper.typeMap(UserDTO.class, User.class);
        typeMap.addMapping(s->s.getPicture(), ((d, o) -> d.setProfilePic((String) o)));
        typeMap.addMapping(s->s.getName(), ((d, o) -> d.setFullName((String) o)));
        return mapper.map(userDTO, User.class);
    }
    public static TaskList getTaskList(TaskListDTO taskListDTO){
        ModelMapper mapper = new ModelMapper();
        TypeMap<TaskListDTO, TaskList> typeMap = mapper.typeMap(TaskListDTO.class, TaskList.class);
        typeMap.addMapping(s->s.getTitle(), (t, o) -> t.setName((String) o));
        return mapper.map(taskListDTO, TaskList.class);
    }
    public static Task getTask(TaskDTO taskDTO){
        ModelMapper mapper = new ModelMapper();
        TypeMap<TaskDTO, Task> typeMap = mapper.typeMap(TaskDTO.class, Task.class);
        typeMap.addMapping(s->s.getNotes(), (t, o) -> t.setDetails((String) o));
//        typeMap.addMapping(s->s.getStatus(), (task, o) -> taskDTO.setStatus((String)  o));
        return mapper.map(taskDTO , Task.class);
    }
}
