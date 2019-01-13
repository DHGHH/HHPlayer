//
// Created by 黄柏雄 on 2018/12/31.
//
#include "Student.h"
#include "string"

Student::Student(std::string name, int age) : mName(name), mAge(age) {}

Student::Student(Student &student) {
    mName = student.mName;
    mAge = student.mAge;
}

Student::Student(std::string name) {
    mName = name;
}

std::ostream & operator<<(std::ostream & os, const Student & student) {
    os << "Name:" << student.mName << " Age:" << student.mAge;
    return os;
}