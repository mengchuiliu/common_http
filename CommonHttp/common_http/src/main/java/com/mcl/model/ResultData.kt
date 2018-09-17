package com.mcl.model

class ResultData<T>() {
    var ExecuteResult: Boolean? = null
    var ReturnMsg: String? = null
    var ReturnString: String? = null
    var ReturnStrings: MutableList<String>? = null
    var ReturnBytes: Any? = null
    var ReturnDataTable: MutableList<T>? = null
    var ReturnDataSet: Any? = null
    var Marker: String? = null
    var IsZip: Boolean? = null
    var ZipType: String? = null
    var UserID: String? = null

    override fun toString(): String {
        return "ResultData(ExecuteResult=$ExecuteResult, ReturnMsg=$ReturnMsg, ReturnString=$ReturnString, ReturnStrings=$ReturnStrings, ReturnBytes=$ReturnBytes, ReturnDataTable=$ReturnDataTable, ReturnDataSet=$ReturnDataSet, Marker=$Marker, IsZip=$IsZip, ZipType=$ZipType, UserID=$UserID)"
    }
}