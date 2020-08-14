package dada.com.kproject.const

class ApiConst {
    companion object{
        const val AUTHORIZATION = "Authorization"

        // Token
        const val GRANT_TYPE = "grant_type"
        const val CLIENT_CREDENTIALS = "client_credentials"
        const val CLIENT_ID = "client_id"
        const val CLIENT_SECRET = "client_secret"

        // CategoryId
        const val CATEGORY_COMPLEX = "KrdH2LdyUKS8z2aoxX"

        const val INITIAL_CATEGORY = 15
        const val SONG_LIST_PAGE = 10

        //Api type
        const val NEW_RELEASE_CATEGORIES = 0
        const val SONG_LIST = 1

        //Paging
        const val PAGING_PAGE_SIZE = 10
        const val PAGING_INITIAL_OFFSET = 0


    }
}