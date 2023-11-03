#ifndef ROSTER_H
#define ROSTER_H

#include "student.h"
#include <string>
using namespace std;

class Roster {
private:
    //the maximum capacity of the roster is resizable here
    static const int rosterCapacity = 10;
    Student* classRosterArray[rosterCapacity];
    
public:
    Roster();
    ~Roster();

    void Parse(string row);
    void Add(string studentID, string firstName, string lastName, string emailAddress,
             int age, int daysInCourse1, int daysInCourse2, int daysInCourse3,
             DegreeProgram degreeProgram);
    void Remove(string studentID);
    
    void PrintAll() const;
    void PrintAverageDaysInCourse(string studentID) const;
    void PrintAllAverageDaysInCourse() const;
    void PrintInvalidEmails() const;  
    void PrintByDegreeProgram(DegreeProgram degreeProgram) const;
};

#endif