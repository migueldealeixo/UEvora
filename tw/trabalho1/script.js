
function clearInputFields() {
    const inputFields = document.querySelectorAll('input');
    inputFields.forEach(input => {
        input.value = '';
    });
}
window.addEventListener('load', clearInputFields);

let page = 0 //Pagina Atual
let lastpage =9 //ultima pagina

function hidebtn(){
    document.getElementById("antes-viagem").style.visibility ="hidden"
    document.getElementById("proximo-pedido").style.visibility = "hidden"
 
}

function nextpage(){
    page = (page +1) %(lastpage +1)
    listarViagens(page)
    
}
function prevpage(){
    page = (page -1) %(lastpage +1)
    listarViagens(page)
    
}
//Nada etico de ter duas funções a fazerem o mesmo mas
function hidebutton(){
    document.getElementById("proximo-pedido").style.visibility="hidden"
    document.getElementById("antes-pedido").style.visibility="hidden"
}
function nextPage(){
    page = (page +1) %(lastpage +1)
    listarPedidos(page)
}
function prevPage(){
    page = (page -1) %(lastpage +1)
    listarPedidos(page)
}

                                            //Listar Pedidos 
                                            
function listarPedidos(jpage) {
    page = jpage
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4) {
            if (this.status === 200) {
                var Response = JSON.parse(this.responseText);
                if (Response.status === 'ok' && Response.pedidos.length > 0) {
                    var nomePassageiro = (document.getElementById("nomeC").value || "").toLowerCase();
                    var results = Response.pedidos.filter(function (pedido) {
                        var nome = (pedido.passageiro || "").toLowerCase();
                        return nome.includes(nomePassageiro);
                    });
                    var output = results.map(function (item) {
                        return `<div>
                                    <div class="item"><strong>Passageiro:</strong> ${item.passageiro}</div>
                                    <div class="item"><strong>Origem:</strong> ${item.origem.place}</div>
                                    <div class="item"><strong>Destino:</strong> ${item.destino.place}</div>
                                    <div class="item"><strong>Data:</strong> ${item.data}</div>
                                </div><hr><br>`;
                    }).join("");

                    document.getElementById("listaClientes").innerHTML = output || "Nenhum pedido encontrado.";
                } else {
                    document.getElementById("listaClientes").innerHTML = "Nenhum pedido encontrado.";
                }
            } else {
                document.getElementById("listaClientes").innerHTML = "Erro ao buscar os pedidos.";
            }
        }
    };


    const antes = document.getElementById("antes-pedido");
    const proximo = document.getElementById("proximo-pedido");
    
    if (antes && proximo) {
        antes.style.visibility = page > 0 ? "visible" : "hidden";
        proximo.style.visibility = page < 9 ? "visible" : "hidden";
    };
    console.log(jpage)
    xhttp.open("POST", "https://magno.di.uevora.pt/tweb/t1/pedidos/list", true);
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhttp.send("page=" + jpage);
}


                                 //Listar Viagens
        function listarViagens(ipage) {
            page = ipage;
            var xhttp = new XMLHttpRequest();

            xhttp.onreadystatechange = function() {
                if (this.readyState === 4) {
                    if (this.status == 200) {
                        var response = JSON.parse(this.responseText);
                        if (response.status === "ok" && response.viagens.length > 0) {
                            var originInput = document.getElementById("pesquisaOrigem").value.toLowerCase();
                            var destinationInput = document.getElementById("pesquisaDestino").value.toLowerCase();

                            var filteredResults = response.viagens.filter(function(viagem) {
                                var origem = (viagem.origem.place || "").toLowerCase();
                                var destino = (viagem.destino.place || "").toLowerCase();
                                return origem.includes(originInput) && destino.includes(destinationInput);
                            });

                            var output = filteredResults.map(function(item) {
                                return `<div>
                                            <div class="item"><strong>Condutor:</strong> ${item.condutor}</div>
                                            <div class="item"><strong>Origem:</strong> ${item.origem.place}</div>
                                            <div class="item"><strong>Destino:</strong> ${item.destino.place}</div>
                                        </div><hr><br>`;
                            }).join("");

                            document.getElementById("listarViagens").innerHTML = output || "Nenhuma Viagem encontrada";
                        } else {
                            document.getElementById("listarViagens").innerHTML = "Nenhuma Viagem encontrada.";
                        }
                    } else {
                        document.getElementById("listarViagens").innerHTML = "Erro ao buscar as viagens.";
                    }
                }
        };


        const antes = document.getElementById("antes-viagem");
        const proximo = document.getElementById("proximo-viagem");
        antes.style.visibility = page > 0 ? "visible" : "hidden";
        proximo.style.visibility = page < 9 ? "visible" : "hidden";

        console.log(ipage);

        xhttp.open("POST", "https://magno.di.uevora.pt/tweb/t1/viagens/list", true);
        xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xhttp.send("page=" + ipage);
    }



let viagemID = 0;
let clienteID = 0

function searchViagens() {

    const origem = document.getElementById("Origem").value;
    const destino = document.getElementById("Destino").value;
    const data = document.getElementById("Data").value;  // Include the date

    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState === 4) {
            if (this.status === 200) {
                const response = JSON.parse(this.responseText);
                if (response.status === "ok" && response.viagens && response.viagens.length > 0) {
                    displayViagens(response.viagens);
                } else {
                    document.getElementById("mostrar").innerHTML = "Nenhuma viagem encontrada.";
                }
            } else {
                document.getElementById("mostrar").innerHTML = "Erro ao buscar as viagens.";
            }
        }
    };

    const url = "https://magno.di.uevora.pt/tweb/t1/viagem/search";
    const requestBody = `origem=${encodeURIComponent(origem)}&destino=${encodeURIComponent(destino)}`;

    console.log(requestBody);  // Log the request body for debugging

    xhttp.open("POST", url, true);
    xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhttp.send(requestBody);
}

function joinViagem(viagemId) {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState === 4) {
            if (this.status === 200) {
                const response = JSON.parse(this.responseText);
                if (response.status === "ok") {
                    alert("Associado à viagem com sucesso!");
                    searchViagens();
                } else {
                    alert("Erro ao associar-se à viagem: " + response.status);
                }
            } else {
                alert("Erro na solicitação de associação à viagem.");
            }
        }
    };

    const url = "https://magno.di.uevora.pt/tweb/t1/viagem/join";
    const requestBody = `v_id=${encodeURIComponent(viagemId)}&passageiro=${encodeURIComponent(clienteID)}`;

    xhttp.open("POST", url, true);
    xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhttp.send(requestBody);
}

function leaveViagem(viagemId) {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState === 4) {
            if (this.status === 200) {
                const response = JSON.parse(this.responseText);
                if (response.status === "ok") {
                    alert("Participação na viagem cancelada com sucesso!");
                    searchViagens();
                } else {
                    alert("Erro ao cancelar participação na viagem: " + response.status);
                }
            } else {
                alert("Erro na solicitação de cancelamento da participação na viagem.");
            }
        }
    };

    const url = "https://magno.di.uevora.pt/tweb/t1/viagem/leave";
    const requestBody = `v_id=${encodeURIComponent(viagemId)}&passageiro=${encodeURIComponent(clienteID)}`;

    xhttp.open("POST", url, true);
    xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhttp.send(requestBody);
}


function displayViagens(viagens) {
    console.log(viagens)
    const output = viagens.map(viagem => `
        <div>
            <div class="item"><strong>Condutor:</strong> ${viagem.condutor}</div>
            <div class = "item"><strong>Passageiro</strong>${viagem.passageiro}</div> 
            <div class="item"><strong>Origem:</strong> ${viagem.origem.place}</div>
            <div class="item"><strong>Destino:</strong> ${viagem.destino.place}</div>
            <div class="item"><strong>Data:</strong> ${viagem.data}</div>
            <button onclick="joinViagem(${viagem.v_id})" class="search-button">Juntar-se</button>
            <button onclick="leaveViagem(${viagem.v_id})" class="search-button">Cancelar</button>
        </div><hr><br>
    `).join("");

    document.getElementById("mostrar").innerHTML = output;
}

//Condutor
                                            //Registar Viagem
    document.addEventListener("DOMContentLoaded", function () {
        const button = document.getElementById("submitOferta");
        button.addEventListener("click", sendViagem);   

    function sendViagem() {
    let nome = document.getElementById("nomeCondutor").value;
    let origem = document.getElementById("origemCondutor").value;
    let destino = document.getElementById("destinoCondutor").value;
    let data = document.getElementById("dataCondutor").value;
    let hora = document.getElementById("timeCondutor").value;

    console.log( nome, origem, destino, data, hora);


    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4) {
            let responseLabel = document.getElementById("responseCondutor");

            try {
                let response = JSON.parse(this.responseText);

                if (this.status == 200 && response.status.startsWith("ok")) {
                    responseLabel.innerHTML = "Viagem Registrada com Sucesso!";
                } else {
                    responseLabel.innerHTML = "Erro: " + (response.status || "Erro desconhecido");
                }
            } catch (e) {
                responseLabel.innerText = "Erro: " + this.status + " " + this.statusText;
            }
        }
    };


    let url = "https://magno.di.uevora.pt/tweb/t1/viagem/add";
    let requestbody =
        "condutor=" + encodeURIComponent(nome) +
        "&origem=" + encodeURIComponent(origem) +
        "&destino=" + encodeURIComponent(destino) +
        "&data=" + encodeURIComponent(data) +
        "&hora=" + encodeURIComponent(hora);

    console.log(requestbody);


    xhttp.open("POST", url, true);
    xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhttp.send(requestbody);
}
    })

                                        //Remover Viagem

function removerViagem() {
    let xhttp = new XMLHttpRequest();


    let viagemID = document.getElementById("codigoViagem").value;
    let condutor = document.getElementById("nomeCondutorID").value;

    xhttp.onreadystatechange = function () {
        if (this.readyState === 4) {
            let rlabel = document.getElementById("RemoveresponseViagem");

            if (this.status === 200) {
                try {
                    let r = JSON.parse(this.responseText);
                    rlabel.innerText = r.status.startsWith("ok") 
                        ? "Viagem Removida com Sucesso!" 
                        : "Erro: " + (r.status || "Erro desconhecido");
                    console.log(r);
                } catch (e) {
                    rlabel.innerText = "Erro ao processar a resposta.";
                    console.error("Erro ao processar JSON:", e);
                }
            } else {
                rlabel.innerText = "Erro: Não foi possível remover a viagem.";
                console.error("Erro ao remover a viagem:", this.status, this.statusText);
            }
        }
    };

    let url = "https://magno.di.uevora.pt/tweb/t1/viagem/remove";

    let rb = "v_id=" + encodeURIComponent(viagemID) + 
             "&condutor=" + encodeURIComponent(condutor);

    console.log( rb);

    
    xhttp.open("POST", url, true);
    xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhttp.send(rb); 
}

                                    //Perfil do Condutor
function searchPerfil() {
    var c_id = $('#IDCondutor').val();

    var url = "https://magno.di.uevora.pt/tweb/t1/condutor/get/" + encodeURIComponent(c_id);
    
    $.ajax({
        url: url,
        method: 'GET',
        dataType: 'json',
        success: function (response2) {
            if (response2.status === 'ok' && response2.result) {
                var condutor = response2.result.condutor;
                var fiabilidade = response2.result.fiabilidade; 
                var viagens = response2.result.viagens || [];
                var viagensHtml = '<ul>';

          
                $('#listarInfoCondutor').html(`
                    <h3><b>Condutor:</b> ${condutor}</h3>
                    <h3><b>Fiabilidade:</b> ${fiabilidade}</h3>
                `);

               
                for (var i = 0; i < Math.min(viagens.length, 10); i++) {
                    var viagem = viagens[i];
                    viagensHtml += `
                        <li>
                            <strong>Viagem ID:</strong> ${viagem.v_id}<br>
                            <strong>Origem:</strong> ${viagem.origem.place}<br>
                            <strong>Destino:</strong> ${viagem.destino.place}<br>
                            <strong>Data:</strong> ${viagem.data}
                        </li>
                    `;
                }
                viagensHtml += '</ul>';
                
 
                $('#listarInfoCondutor').append(viagensHtml);
            } else {
                $('#listarInfoCondutor').html('<p>Perfil não encontrado</p>');
            }
        },
        error: function (xhr, status, error) {
            console.error("Erro:", status, error);
            console.error("Resposta do Servidor:", xhr.responseText);
            $('#listarInfoCondutor').html('<p>Erro ao buscar o perfil do condutor.</p>');
        }
    });
}


//Area do Passageiro
                                        //Perfil do Passageiro
function searchPassageiro() {
    var id = $('#IDPassageiro').val();
    var url = "https://magno.di.uevora.pt/tweb/t1/passageiro/get/" + encodeURIComponent(id);
    console.log("Requesting URL:", url);

    $.ajax({
        url: url,
        method: 'GET',
        dataType: 'json',
        success: function (response) {
            console.log("Response Data:", response);
            if (response.status === "ok" && response.result) {
                var passageiro = response.result.passageiro;
                var fiabilidade = response.result.fiabilidade;
                var viagens = response.result.viagens || [];
                var viagensHtml = `<ul>`; // Inicializar variável

                $('#listarInfo').html(`
                    <h3><b>Passageiro:</b> ${passageiro}</h3>
                    <h3><b>Fiabilidade:</b> ${fiabilidade}</h3>
                `);

                for (var i = 0; i < Math.min(viagens.length, 10); i++) {
                    var viagem = viagens[i];
                    viagensHtml += `
                        <li>
                            <strong>Viagem ID:</strong> ${viagem.v_id}<br>
                            <strong>Origem:</strong> ${viagem.origem.place}<br>
                            <strong>Destino:</strong> ${viagem.destino.place}<br>
                            <strong>Data:</strong> ${viagem.data}
                        </li>
                    `;
                }

                viagensHtml += '</ul>';
                $('#listarInfo').append(viagensHtml);
            } else {
                $('#listarInfo').html('<p>Perfil não encontrado.</p>');
            }
        },
        error: function (xhr, status, error) {
            console.error("Error Status:", status);
            console.error("Error Message:", error);
            console.log("Response Text:", xhr.responseText);
            $('#listarInfo').html('Erro: ' + error);
        }
    });
}

                                            //Enviar Pedido

        document.addEventListener("DOMContentLoaded", function () {
            const button = document.getElementById("submitButton");
            button.addEventListener("click", sendPedido);
        
            function sendPedido(event) {
                event.preventDefault();
        
                let nome = document.getElementById("nome").value;
                let origem = document.getElementById("origem").value;
                let destino = document.getElementById("destino").value;
                let data = document.getElementById("data").value;
                let hora = document.getElementById("hora").value;
        
                console.log(nome, origem, destino, data, hora);
        
                let xhttp = new XMLHttpRequest();
                xhttp.onreadystatechange = function () {
                    if (this.readyState == 4) {
                        let ResponseLabel = document.getElementById("responseLabel");
                        if (this.status == 200) {
                            try {
                                let response = JSON.parse(this.responseText);
                                ResponseLabel.innerText = response.status.startsWith("ok")
                                    ? "Pedido registrado com sucesso!"
                                    : "ERRO: " + (response.status || "Erro desconhecido");
                            } catch (e) {
                                responseLabel.innerText = "Erro a realizar o pedido";
                            }
                        } else {
                            responseLabel.innerText = "ERRO: " + this.status + " " + this.statusText;
                        }
                    }
                };
        
                let url = "https://magno.di.uevora.pt/tweb/t1/pedido/add";
                let requestBody = "passageiro=" + encodeURIComponent(nome) +
                                  "&origem=" + encodeURIComponent(origem) +
                                  "&destino=" + encodeURIComponent(destino) +
                                  "&data=" + encodeURIComponent(data) +
                                  "&hora=" + encodeURIComponent(hora);
        
                xhttp.open("POST", url, true);
                xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                xhttp.send(requestBody);
            }
        });


                                        //Remover Pedido
    function removerPedido() {
        let xhttp = new XMLHttpRequest();
        let registroID = document.getElementById("codigopedido").value
        let passageiro = document.getElementById("nomePassageiro").value
        xhttp.onreadystatechange = function () {
            if (this.readyState == 4) {
                console.log("Response Text:", this.responseText);
                let responseLabel= document.getElementById("Removeresponse")
                if (this.status === 200) {
                    try {
                        let response = JSON.parse(this.responseText);
                        responseLabel.innerText = response.status.startsWith("ok")
                        ? "Pedido removido com sucesso!"
                        : "ERRO: " + (response.status || "Erro desconhecido");
                        console.log("Server Response:", response);
                        if (response.status === "ok") {
                            console.log("Pedido removido com sucesso.");
                        } else {
                            console.error("Erro no servidor:", response.message || "Resposta inesperada.");
                        }
                    } catch (error) {
                        console.error("Erro ao analisar a resposta do servidor:", error);
                    }
                } else {
                    console.error("Erro na solicitação. Status:", this.status);
                }
            }
        };
    
        let url = "https://magno.di.uevora.pt/tweb/t1/pedido/remove/";
        let requestBody = `p_id=${encodeURIComponent(registroID)}&passageiro=${encodeURIComponent(passageiro)}`;
        console.log("Request Body:", requestBody);
    
        xhttp.open("POST", url, true);
        xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xhttp.send(requestBody);
    }   