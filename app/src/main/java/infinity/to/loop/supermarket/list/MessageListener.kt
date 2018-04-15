package infinity.to.loop.supermarket.list

import infinity.to.loop.supermarket.models.Message


interface MessageListener {

    fun onNewMessage(message: Message)
}