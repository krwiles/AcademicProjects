#include "roster.h"
#include <iostream>
#include <algorithm>
using namespace std;

Roster::Roster() {
    //initialize classRosterArray
    for (int i = 0; i < rosterCapacity; i++) {
        this->classRosterArray[i] = nullptr;
    }
}

Roster::~Roster() {
    //delete all Students in classRosterArray
    for (int i = 0; i < rosterCapacity; i++) {
        delete this->classRosterArray[i];
        //deleting any nullptr does nothing and will not cause an exception
    }
}

void Roster::Parse(string  row) {
    size_t rhs = row.find(",");
    string studentID = row.substr(0, rhs);

    size_t lhs = rhs + 1;
    rhs = row.find(",", lhs);
    string firstName = row.substr(lhs, rhs - lhs);
    
    lhs = rhs + 1;
    rhs = row.find(",", lhs);
    string lastName = row.substr(lhs, rhs - lhs);

    lhs = rhs + 1;
    rhs = row.find(",", lhs);
    string emailAddress = row.substr(lhs, rhs - lhs);

    lhs = rhs + 1;
    rhs = row.find(",", lhs);
    int age = stoi(row.substr(lhs, rhs - lhs));

    lhs = rhs + 1;
    rhs = row.find(",", lhs);
    int daysInCourse1 = stoi(row.substr(lhs, rhs - lhs));

    lhs = rhs + 1;
    rhs = row.find(",", lhs);
    int daysInCourse2 = stoi(row.substr(lhs, rhs - lhs));

    lhs = rhs + 1;
    rhs = row.find(",", lhs);
    int daysInCourse3 = stoi(row.substr(lhs, rhs - lhs));

    lhs = rhs + 1;
    rhs = row.find(",", lhs);
    string strDP = row.substr(lhs, rhs - lhs);
    transform(strDP.begin(), strDP.end(), strDP.begin(), ::toupper);
    
    //matching strDP to the corresponding enum value
    DegreeProgram degreeProgram = NONE;
    for (int i = 0; i < 3; i++) {
        if (strDP == degreeProgramStrings[i]) {
            degreeProgram = static_cast<DegreeProgram>(i);
            break;
        }
    }

    Add(studentID, firstName, lastName, emailAddress, age, 
        daysInCourse1, daysInCourse2, daysInCourse3, degreeProgram);
}

void Roster::Add(string studentID, string firstName, string lastName, string emailAddress, 
                 int age, int daysInCourse1, int daysInCourse2, int daysInCourse3, 
                 DegreeProgram degreeProgram) {
    
    int daysInCourses[3] = { daysInCourse1, daysInCourse2, daysInCourse3 };

    //putting the new Student in the first available classRosterArray location and returning
    for (int i = 0; i < rosterCapacity; i++) {
        if (this->classRosterArray[i] == nullptr) {
            this->classRosterArray[i] = new Student(studentID, firstName, lastName, emailAddress, 
                                                    age, daysInCourses, degreeProgram);
            return;
        }
    }
    
    //the loop is completed without finding an available location
    cout << "The classRosterArray has reached maximum capacity." << endl;
}

void Roster::Remove(string studentID) {
    string currID;
    
    //checking all studentIDs
    for (int i = 0; i < rosterCapacity; i++) {
        if (this->classRosterArray[i] != nullptr) {
            currID = this->classRosterArray[i]->GetStudentID();
            
            //deleting the Student with matching ID and returning
            if (currID == studentID) {
                delete this->classRosterArray[i];
                this->classRosterArray[i] = nullptr;
                cout << "The studentID: " << studentID << " was successfully deleted." << endl;
                return;
            }
        }
    }

    //the loop is completed without finding a matching studentID
    cout << "The studentID: " << studentID << " was not found for deletion." << endl;
}

void Roster::PrintAll() const {
    //calling Print() on all Students in the array
    for (int i = 0; i < rosterCapacity; i++) {
        if (this->classRosterArray[i] != nullptr) {
            this->classRosterArray[i]->Print();
        }
    }
}

void Roster::PrintAverageDaysInCourse(string studentID) const {
    string currID;

    //checking all studentIDs
    for (int i = 0; i < rosterCapacity; i++) {
        if (this->classRosterArray[i] != nullptr) {
            currID = this->classRosterArray[i]->GetStudentID();

            //finding average for the correct Student and returning
            if (currID == studentID) {
                //getting daysInCourses
                int daysInCourses[3] = {0};
                for (int n = 0; n < 3; n++) {
                    daysInCourses[n] = this->classRosterArray[i]->GetDaysInCourses()[n];
                }
                //summing daysInCourses
                int sum = 0;
                for (int n = 0; n < 3; n++) {
                    sum += daysInCourses[n];
                }
                //calculating average
                int averageDaysInCourse = sum / 3;

                cout << "Student ID: " << currID << ", average days in course is: "
                    << averageDaysInCourse << endl;

                return;
            }
        }
    }

    //the loop is completed without finding a matching studentID
    cout << "The studentID: " << studentID << " was not found for average days in course." << endl;
}

void Roster::PrintAllAverageDaysInCourse() const {
    //calling PrintAverageDaysInCourse() for all Students in the array using studentIDs
    for (int i = 0; i < rosterCapacity; i++) {
        if (this->classRosterArray[i] != nullptr) {
            PrintAverageDaysInCourse(this->classRosterArray[i]->GetStudentID());
        }
    }
}

void Roster::PrintInvalidEmails() const {
    string currEmail;

    //checking all emailAddresses
    for (int i = 0; i < rosterCapacity; i++) {
        if (this->classRosterArray[i] != nullptr) {
            currEmail = this->classRosterArray[i]->GetEmailAddress();

            //checking for spaces
            if (currEmail.find(" ") != string::npos) {
                cout << currEmail << " - is invalid." << endl;
                continue;
            }

            //checking for @
            int atPos = currEmail.find("@");
            if (atPos == string::npos) {
                cout << currEmail << " - is invalid." << endl;
                continue;
            }

            //checking for . after the @
            if (currEmail.find(".", atPos) == string::npos) {
                cout << currEmail << " - is invalid." << endl;
                continue;
            }
        }
    }
}

void Roster::PrintByDegreeProgram(DegreeProgram degreeProgram) const {
    for (int i = 0; i < rosterCapacity; i++) {
        //calling Print() on all Students with matching degreeProgram
        if (this->classRosterArray[i] != nullptr && 
            this->classRosterArray[i]->GetDegreeProgram() == degreeProgram) {
            //the if condition will not cause an exception 
            //because && will not evaluate the right side when the left side is false
            this->classRosterArray[i]->Print();
        }
    }
}
