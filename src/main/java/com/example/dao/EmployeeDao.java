package com.example.dao;

import com.example.modle.Employee;
import com.example.modle.Page;

import java.util.List;

public interface EmployeeDao {
    // 获取所有员工（不分页）
    List<Employee> getAllEmployees();

    // 分页获取员工
    Page<Employee> getEmployeesPage(int pageNum, int pageSize);

    // 根据id查询员工信息
    Employee findById(Integer id);

    // 添加员工信息
    boolean add(Employee employee);

    // 更新员工信息
    boolean updateById(Employee employee);

    // 删除员工信息
    boolean delete(Integer id);

    // 根据经理ID获取所有下属员工
    List<Employee> getEmployeesByManagerId(Integer managerId);

    // 获取没有经理的员工
    List<Employee> getEmployeesWithoutManager();
}