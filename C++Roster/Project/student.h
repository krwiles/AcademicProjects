#ifndef STUDENT_H
#define STUDENT_H

#include "degree.h"
#include <string>
using namespace std;

class Student {
private:
    string studentID;
    string firstName;
    string lastName;
    string emailAddress;
    int age;
    int daysInCourses[3];
    DegreeProgram degreeProgram;

public:
    Student();
    Student(string studentID, string firstName, string lastName, string emailAddress,
            int age, int daysInCourses[3], DegreeProgram degreeProgram);
    ~Student();

    void SetStudentID(string studentID);
    void SetFirstName(string firstName);
    void SetLastName(string lastName);
    void SetEmailAddress(string emailAddress);
    void SetAge(int age);
    void SetDaysInCourses(int daysInCourses[3]);
    void SetDegreeProgram(DegreeProgram degreeProgram);

    string GetStudentID() const;
    string GetFirstName() const;
    string GetLastName() const;
    string GetEmailAddress() const;
    int GetAge() const;
    int* GetDaysInCourses();
    DegreeProgram GetDegreeProgram() const;

    void Print();
};

#endif
