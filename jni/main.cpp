#include <jni.h>
#include <string.h>
#include <iostream>
#include <windows.h>
#include <mysql.h>
#include "sha256.h"
#include<sstream>


using namespace std;



class UserAccount{
private:
    string username;
    string email;
    string password;

public:
    UserAccount(string Username, string Email, string Password){
        this->username = Username;
        this->email = Email;
        this->password = Password;

    }

    string getEmail()
    {
        return this->email;
    }

    string getPassword()
    {
        password = sha256(password);
        return password;
    }

    string getUsername()
    {
        return this->username;
    }


};

    void database(UserAccount* user)
    {
        //creating database connection
        MYSQL* conn;
        conn = mysql_init(0);
        conn = mysql_real_connect(conn,"192.168.137.202","admin","adminSneabs","ICP_Project2",0,NULL,0);

        int qstate = 0; //stores query status after execution; 0 for successful and non-zero for failed

        //inserting data into database
        stringstream ss;

        ss<< "INSERT INTO useraccount VALUES('"<<user->getUsername()<<"','"<<user->getEmail()<<"','"<<user->getPassword()<<"',"<<0.00<<")";
        string query = ss.str(); //making a string query
        const char* q = query.c_str(); // converting string query to char because mysql_query function accepts only char
        qstate = mysql_query(conn, q);
        if(qstate == 0){
            cout<<"Record inserted"<<endl;
        } else{
            cout<<"Failed"<<endl;
        }
    }

    bool Login(string email, string password)
    {
        MYSQL* conn;
        MYSQL_ROW row;
        MYSQL_RES* res;
        conn = mysql_init(0);
        conn = mysql_real_connect(conn,"192.168.137.202","admin","adminSneabs","ICP_Project2",0,NULL,0);

        bool state = false;
        stringstream sa;
        sa<<"select * from useraccount where Email = '"<<email<<"'";
        string query = sa.str();
        const char* q = query.c_str();
        int qstate = 0;
        qstate = mysql_query(conn,q);
        if(qstate ==0){
            res = mysql_store_result(conn);
            while(row = mysql_fetch_row(res))
            {
                if((email==row[1]) && (sha256(password)==row[2])){
                        state = true;
                }
            }
        } else{
            cout<<"Failed"<<endl;
        }

        return state;
    }


int main()
{
    UserAccount* user1 = new UserAccount("Koo Danny","kooashesi.edu.gh","danny");
    //cout<<user1->getUsername();

    //cout<<Login("koo.danny@ashesi.edu.gh","danny")<<endl;

    database(user1);
    return 0;
}
