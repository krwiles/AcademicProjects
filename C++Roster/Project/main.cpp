#include <iostream>
#include <string>

#include "roster.h"
using namespace std;

int main() {
    const string studentData[] = {
        "A1,John,Smith,John1989@gm ail.com,20,30,35,40,SECURITY",
        "A2,Suzan,Erickson,Erickson_1990@gmailcom,19,50,30,40,NETWORK",
        "A3,Jack,Napoli,The_lawyer99yahoo.com,19,20,40,33,SOFTWARE",
        "A4,Erin,Black,Erin.black@comcast.net,22,50,58,40,SECURITY",
        "A5,Kurtis,Wiles,kwiles2@my.wgu.edu,22,30,20,34,SOFTWARE" 
    };
    
    cout << "Language: C++" << endl;
    cout << "Name: Kurtis Wiles" << endl;
    
    Roster classRoster;
    for (int i = 0; i < 5; i++) {
        classRoster.Parse(studentData[i]);
    }

    cout << "\nDisplaying all students:\n" << endl;
    classRoster.PrintAll();

    cout << "\nDisplaying invalid email addresses:\n" << endl;
    classRoster.PrintInvalidEmails();

    cout << endl;
    //classRosterArray is a private data member, so a function is used to print
    //    the average days in course for all of the Students
    classRoster.PrintAllAverageDaysInCourse();

    cout << "\nShowing students in degree program: SOFTWARE\n" << endl;
    classRoster.PrintByDegreeProgram(SOFTWARE);

    cout << "\nRemoving A3\n" << endl;
    classRoster.Remove("A3");
    classRoster.PrintAll();

    cout << "\nRemoving A3 again\n" << endl;
    classRoster.Remove("A3");

    cout << "DONE." << endl;
    //Roster's destructor will be called when main() returns
    return 0;
}