#include "student.h"
#include <iostream>
using namespace std;

Student::Student() {
    this->studentID = "None";
    this->firstName = "None";
    this->lastName = "None";
    this->emailAddress = "None";
    this->age = 0;
    for (int i = 0; i < 3; i++) {
        this->daysInCourses[i] = 0;
    }
    this->degreeProgram = NONE;
}

Student::Student(string studentID, string firstName, string lastName, string emailAddress,
                 int age, int daysInCourses[3], DegreeProgram degreeProgram) {
    this->studentID = studentID;
    this->firstName = firstName;
    this->lastName = lastName;
    this->emailAddress = emailAddress;
    this->age = age;
    for (int i = 0; i < 3; i++) {
        this->daysInCourses[i] = daysInCourses[i];
    }
    this->degreeProgram = degreeProgram;
}

Student::~Student() {
    //the destrutor has no dynamically allocated memory to clear
}

void Student::SetStudentID(string studentID) {
    this->studentID = studentID;
}

void Student::SetFirstName(string firstName) {
    this->firstName = firstName;
}

void Student::SetLastName(string lastName) {
    this->lastName = lastName;
}

void Student::SetEmailAddress(string emailAddress) {
    this->emailAddress = emailAddress;
}

void Student::SetAge(int age) {
    this->age = age;
}

void Student::SetDaysInCourses(int daysInCourses[3]) {
    for (int i = 0; i < 3; i++) {
        this->daysInCourses[i] = daysInCourses[i];
    }
}

void Student::SetDegreeProgram(DegreeProgram degreeProgram) {
    this->degreeProgram = degreeProgram;
}

string Student::GetStudentID() const {
    return this->studentID;
}

string Student::GetFirstName() const {
    return this->firstName;
}

string Student::GetLastName() const {
    return this->lastName;
}

string Student::GetEmailAddress() const {
    return this->emailAddress;
}

int Student::GetAge() const {
    return this->age;
}

int* Student::GetDaysInCourses() {
    int* arrayPointer = this->daysInCourses;
    return arrayPointer;
}

DegreeProgram Student::GetDegreeProgram() const {
    return this->degreeProgram;
}

void Student::Print() {
    cout << GetStudentID()
        << "\t" << GetFirstName()
        << "\t    " << GetLastName()
        << "\t    " << GetAge()
        << " \t {" << GetDaysInCourses()[0]
        << ", " << GetDaysInCourses()[1]
        << ", " << GetDaysInCourses()[2]
        << "} \t " << degreeProgramStrings[GetDegreeProgram()] << endl;
}