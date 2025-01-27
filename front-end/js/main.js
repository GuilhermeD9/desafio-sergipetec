const apiBaseUrl = 'http://localhost:8080';

// Carrega os veículos ao carregar a página
document.addEventListener('DOMContentLoaded', filtrarVeiculos);

async function carregarVeiculos(url) {
    const apiUrl = url || `${apiBaseUrl}/veiculos/listartodos`;
    try {
        const response = await fetch(apiUrl);
        if (!response.ok) throw new Error('Erro ao carregar veículos.');

        const veiculos = await response.json();
        console.log('Dados recebidos:', veiculos);

        const tableBody = document.getElementById('veiculos-table-body');
        tableBody.innerHTML = veiculos.map(veiculo => criarLinhaTabela(veiculo)).join('');

    } catch (error) {
        console.error('Erro:', error);
        Swal.fire('Erro!', 'Veículo(s) não encontrado(s)', 'error')
    }    
}

function criarLinhaTabela(veiculo) {
    return `
        <tr>
            <td>${veiculo.id}</td>
            <td>${veiculo.modelo}</td>
            <td>${veiculo.fabricante}</td>
            <td>${veiculo.cor}</td>
            <td>${veiculo.ano}</td>
            <td>${veiculo.tipo}</td>
            <td class="actions">
                <button class="edit" onclick="editarVeiculo(${veiculo.id})">Editar</button>
                <button class="delete" onclick="deletarVeiculo(${veiculo.id})">Excluir</button>
            </td>
        `;
}

async function filtrarVeiculos() {
    const tipo = document.getElementById('filter-tipo').value;
    const modelo = document.getElementById('filter-modelo').value;
    const cor = document.getElementById('filter-cor').value;
    const ano = document.getElementById('filter-ano').value;

    const filtros = {};
    if (tipo) filtros.tipo = tipo;
    if (modelo) filtros.modelo = modelo;
    if (cor) filtros.cor = cor;
    if (ano) filtros.ano = ano;

    const params = new URLSearchParams(filtros).toString();
    const url = `${apiBaseUrl}/veiculos/busca-especifica?${params}`;
    await carregarVeiculos(url);
}

function abrirFormularioCadastro() {
    Swal.fire({
        title: 'Cadastrar Veículo',
        html: gerarFormularioCadastro(),
        showCancelButton: true,
        confirmButtonText: 'Salvar',
        customClass: {
            popup: 'popup-class',
            confirmButton: 'btn-confirm',
            cancelButton: 'btn-cancel'
        },
        preConfirm: obterDadosFormularioCadastro
    }).then(result => {
        if (result.isConfirmed) {
            const dados = result.value;
            const url = `${apiBaseUrl}/veiculos/cadastrar/${dados.tipo.toLowerCase()}`;
            salvarVeiculo(url, dados)
        }
    });
}

function gerarFormularioCadastro() {
    return `
            <div style="display: flex; flex-direction: column; gap: 10px; text-align: left;">
                <label for="tipo-veiculo" style="font-weight: bold;">Tipo:</label>
                <select id="tipo-veiculo" onchange="atualizarFormularioCadastro()" style="padding: 5px; border: 1px solid #ccc; border-radius: 4px;">
                    <option value="">Selecione</option>
                    <option value="Carro">Carro</option>
                    <option value="Moto">Moto</option>
                </select>

                <label for="modelo" style="font-weight: bold;">Modelo:</label>
                <input type="text" id="modelo" placeholder="Modelo" style="padding: 5px; border: 1px solid #ccc; border-radius: 4px;">

                <label for="fabricante" style="font-weight: bold;">Fabricante:</label>
                <input type="text" id="fabricante" placeholder="Fabricante" style="padding: 5px; border: 1px solid #ccc; border-radius: 4px;">

                <label for="cor" style="font-weight: bold;">Cor:</label>
                <input type="text" id="cor" placeholder="Cor" style="padding: 5px; border: 1px solid #ccc; border-radius: 4px;">

                <label for="ano" style="font-weight: bold;">Ano:</label>
                <input type="number" id="ano" placeholder="Ano" style="padding: 5px; border: 1px solid #ccc; border-radius: 4px;">

                <label for="preco" style="font-weight: bold;">Preço:</label>
                <input type="number" id="preco" placeholder="Preço" style="padding: 5px; border: 1px solid #ccc; border-radius: 4px;">

                <div id="campos-extras" style="margin-top: 10px;"></div>
            </div>
        `;
}

function obterDadosFormularioCadastro() {
    const tipo = document.getElementById('tipo-veiculo').value;
    const modelo = document.getElementById('modelo').value;
    const cor = document.getElementById('cor').value;
    const fabricante = document.getElementById('fabricante').value;
    const ano = document.getElementById('ano').value;
    const preco = document.getElementById('preco').value;

    if (!tipo || !modelo || !fabricante || !cor || !ano || !preco) {
        Swal.showValidationMessage('Por favor, preencha todos os campos.');
        return false;
    }

    let extras = {};
    if (tipo === 'Carro') {
        const quantidadePortas = document.getElementById('quantidade-portas').value;
        const tipoCombustivel = document.getElementById('tipo-combustivel').value;
        if (!quantidadePortas || !tipoCombustivel) {
            Swal.showValidationMessage('Por favor, preencha todos os campos extras para Carro.');
            return false;
        }
        extras = { quantidadePortas, tipoCombustivel };
    } else if (tipo === 'Moto') {
        const cilindrada = document.getElementById('cilindrada').value;
        if (!cilindrada) {
            Swal.showValidationMessage('Por favor, preencha o campo de Cilindrada para Moto.');
            return false;
        }
        extras = { cilindrada };
    }

    return { tipo, modelo, cor, fabricante, ano, preco, ...extras };
}

function salvarVeiculo(url, dados) {
    fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(dados)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Erro ao cadastrar o veículo.');
            }
            return response.json();
        })
        .then(() => {
            Swal.fire('Sucesso', 'Veículo cadastrado com sucesso!', 'success');
            carregarVeiculos();
        })
        .catch(error => {
            console.error(error);
            Swal.fire('Erro', 'Falha ao cadastrar o veículo.', 'error');
        });
}

function atualizarFormularioCadastro() {
    const tipo = document.getElementById('tipo-veiculo').value;
    const camposExtras = document.getElementById('campos-extras');
    camposExtras.innerHTML = tipo === 'Carro' ? gerarCamposExtrasCarro() : tipo === 'Moto' ? gerarCamposExtrasMoto() : '';
}

function gerarCamposExtrasCarro() {
    return `
            <label for="quantidade-portas" style="font-weight: bold;">Quantidade de Portas:</label>
            <input type="number" id="quantidade-portas" placeholder="Portas" style="padding: 5px; border: 1px solid #ccc; border-radius: 4px;">
            <br>
            <label for="tipo-combustivel" style="font-weight: bold;">Tipo de Combustível:</label>
            <select id="tipo-combustivel" style="padding: 5px; border: 1px solid #ccc; border-radius: 4px;">
                <option value="Gasolina">Gasolina</option>
                <option value="Etanol">Etanol</option>
                <option value="Flex">Flex</option>
                <option value="Diesel">Diesel</option>
            </select>
        `;
}

function gerarCamposExtrasMoto() {
    return `
            <label for="cilindrada" style="font-weight: bold;">Cilindrada:</label>
            <input type="number" id="cilindrada" placeholder="Cilindrada" style="padding: 5px; border: 1px solid #ccc; border-radius: 4px;">
        `;
}

function editarVeiculo(id) {
    fetch(`${apiBaseUrl}/veiculos/listar/${id}`)
        .then(response => response.json())
        .then(veiculo => {
            Swal.fire({
                title: 'Editar Veículo',
                html: gerarFormularioCadastroEdit(veiculo),
                showCancelButton: true,
                confirmButtonText: 'Salvar',
                customClass: {
                    popup: 'popup-class',
                    confirmButton: 'btn-confirm',
                    cancelButton: 'btn-cancel'
                },
                preConfirm: obterDadosFormularioCadastro
            }).then(result => {
                if (result.isConfirmed) {
                    const dados = result.value;
                    console.log('Dados do veículo:', dados);
                    const url = `${apiBaseUrl}/veiculos/atualizar/${id}`;
                    fetch(url, {
                        method: 'PUT',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(dados)
                    })
                        .then(response => {
                            if (!response.ok) {
                                throw new Error('Erro ao atualizar o veículo.');
                            }
                            return response.json();
                        })
                        .then(() => {
                            Swal.fire('Sucesso', 'Informações atualizadas com sucesso!', 'success');
                            carregarVeiculos();
                        })
                        .catch(error => {
                            console.error(error);
                            Swal.fire('Erro', 'Falha ao atualizar o veículo.', 'error');
                        });
                }
            });
        })
        .catch(error => {
            console.error('Erro ao carregar os dados do veículo:', error);
            Swal.fire('Erro', 'Falha ao carregar os dados do veículo para edição.', 'error');
        });
}

function gerarFormularioCadastroEdit(veiculo) {
    return `
        <div style="display: flex; flex-direction: column; gap: 10px; text-align: left;">
            <label for="tipo-veiculo" style="font-weight: bold;">Tipo:</label>
            <select id="tipo-veiculo" onchange="atualizarFormularioCadastro()" style="padding: 5px; border: 1px solid #ccc; border-radius: 4px;">
                <option value="">Selecione</option>
                <option value="Carro">Carro</option>
                <option value="Moto">Moto</option>
            </select>
            <label for="modelo" style="font-weight: bold;">Modelo:</label>
            <input type="text" id="modelo" value="${veiculo.modelo}" style="padding: 5px; border: 1px solid #ccc; border-radius: 4px;">
            <label for="fabricante" style="font-weight: bold;">Fabricante:</label>
            <input type="text" id="fabricante" value="${veiculo.fabricante}" style="padding: 5px; border: 1px solid #ccc; border-radius: 4px;">
            <label for="cor" style="font-weight: bold;">Cor:</label>
            <input type="text" id="cor" value="${veiculo.cor}" style="padding: 5px; border: 1px solid #ccc; border-radius: 4px;">
            <label for="ano" style="font-weight: bold;">Ano:</label>
            <input type="number" id="ano" value="${veiculo.ano}" style="padding: 5px; border: 1px solid #ccc; border-radius: 4px;">
            <label for="preco" style="font-weight: bold;">Preço:</label>
            <input type="number" id="preco" value="${veiculo.preco}" style="padding: 5px; border: 1px solid #ccc; border-radius: 4px;">
            <div id="campos-extras" style="margin-top: 10px;"></div>
        </div>
    `;
}

async function deletarVeiculo(id) {
    const confirmDelete = await Swal.fire({
        title: 'Excluir Veículo?',
        text: 'Você tem certeza que deseja excluir este veículo?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Excluir',
        cancelButtonText: 'Cancelar'
    });

    if (confirmDelete.isConfirmed) {
        fetch(`${apiBaseUrl}/veiculos/deletar/${id}`, { method: 'DELETE' })
            .then(response => {
                if (!response.ok) throw new Error('Erro ao excluir o veículo.');
                Swal.fire('Sucesso', 'Veículo excluído com sucesso!', 'success');
                carregarVeiculos();
            })
            .catch(error => {
                console.error('Erro ao excluir veículo:', error);
                Swal.fire('Erro', 'Falha ao excluir o veículo.', 'error');
            });
    }
}