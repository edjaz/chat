const ChatClient = function () {

    let subsribe = null;
    let open = null;
    let asFreeChat = false;
    let chat = null;

    const _subsribeEvent = (e) => {
        console.log("_subsribeEvent", e.data);
    };

    const _onFreeChat = (e) => {
        console.log("_onFreeChat", e.data);
        asFreeChat = true;
    };

    const _onNotAvailableChat = (e) => {
        console.log("_onNotAvailableChat", e.data);
        asFreeChat = false;
    };

    const _openEvent = (e) => {
        console.log("_openEvent");
        chat = JSON.parse(e.data);
    };

    const _init = () => {
        subsribe = new EventSource("/chats/client/subscribe");
        subsribe.onmessage = _subsribeEvent;
        subsribe.addEventListener("free", _onFreeChat, false);
        subsribe.addEventListener("notAvailable", _onNotAvailableChat, false);
    };


    const _open = () => {
        open = new EventSource("/chats/client/open");
        subsribe.onmessage = _openEvent;

    };

    return {
        init: _init(),
        open: _open()
    }
}();

ChatClient.init();
