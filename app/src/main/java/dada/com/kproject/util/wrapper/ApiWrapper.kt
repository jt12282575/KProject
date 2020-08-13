package dada.com.kproject.util.wrapper

import dada.com.kproject.const.ApiConst.Companion.NEW_RELEASE_CATEGORIES

data class ApiWrapper (val apiType:Int = NEW_RELEASE_CATEGORIES
                       ,val offset:Int = 0,val limit:Int = 10,var firstLoad:Boolean = true)