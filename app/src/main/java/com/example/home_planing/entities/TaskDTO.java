package com.example.home_planing.entities;

public class TaskDTO {

    private Long task_id;
    private String terminado;

    public TaskDTO() {}
    public TaskDTO(Long id,String terminado ) {
        this.task_id = id;
        this.terminado = terminado;

    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + task_id +
                ", terminnado='" + terminado + '}';
    }

    public Long getId() {
        return task_id;
    }

    public void setId(Long id) {
        this.task_id = id;
    }

    public String getTerminado() {
        return terminado;
    }

    public void setTerminado(boolean String) {
        this.terminado = terminado;
    }

}
