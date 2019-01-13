//
// Created by 黄柏雄 on 2018/12/31.
//

#ifndef GO2019_STUDENT_H
#define GO2019_STUDENT_H
#include "string"
#include "iostream"
class Student{
    private:
    std::string mName;
    int mAge;
    public:
    Student(Student &);
    Student(std::string name);
    Student(std::string name, int age);
    int getAge(){
        return mAge;
    };
    std::string getName(){
        return mName;
    }
    friend std::ostream & operator<<(std::ostream & os, const Student & student);
};
#endif //GO2019_STUDENT_H