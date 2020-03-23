package com.example.lancrowd.activity.modal


import java.io.Serializable


class Profile_Detail_Modal(
    var work_details: ArrayList<Work_Detail> = ArrayList<Work_Detail>(),
    var school_details: ArrayList<School_Detail> = ArrayList<School_Detail>(),
    var user_university_details: ArrayList<University_Detail> = ArrayList<University_Detail>(),
    var user_skills: ArrayList<University_Detail> = ArrayList<University_Detail>(),
    var basic_info: ArrayList<Basic_Detail> = ArrayList<Basic_Detail>(),
    var check_friend: ArrayList<Check_Friend_Detail> = ArrayList<Check_Friend_Detail>(),
    var ReqStatus: ArrayList<Request_Status_Detail> = ArrayList<Request_Status_Detail>(),
    var relationship_status: ArrayList<Relation_Status_Detail> = ArrayList<Relation_Status_Detail>()) : Serializable {




    class Work_Detail(val position: String, val company_name: String, val description: String) : Serializable


    class School_Detail(val school: String, val description: String) : Serializable
    class University_Detail(val university: String,val course: String,val degree: String,val description: String,val graduated: String) : Serializable
    class Skill_Detail(val user_skill: String) : Serializable
    class Relation_Status_Detail(val relationship_status: String) : Serializable
    class Basic_Detail(val user_id: String, val email: String, val phone: String, val bio_graphy: String, val profile_picture: String, val cover_photo: String, val gender: String, val date_of_birth: String) : Serializable
    class Check_Friend_Detail(val description: String) : Serializable
    class Request_Status_Detail() : Serializable




}