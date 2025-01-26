const apiBaseUrl = 'http://localhost:8080';

// Carregar os veículos ao carregar a página
document.addEventListener('DOMContentLoaded', carregarVeiculos);

async function carregarVeiculos() {
    const response = await fetch(`${apiBaseUrl}/veiculos/listartodos`);
    const veiculos = await response.json();
    const tableBody = document.getElementById('veiculos-table-body');
    tableBody.innerHTML = '';

    veiculos.forEach(veiculo => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${veiculo.id}</td>
            <td>${veiculo.tipo}</td>
            <td>${veiculo.modelo}</td>
            <td>${veiculo.cor}</td>
            <td>${veiculo.ano}</td>
            <td class="actions">
                <button class="edit" onclick="editarVeiculo(${veiculo.id})">Editar</button>
                <button class="delete" onclick="deletarVeiculo(${veiculo.id})">Excluir</button>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

async function filtrarVeiculos() {
    const tipo = document.getElementById('filter-tipo').value;
    const modelo = document.getElementById('filter-modelo').value;
    const cor = document.getElementById('filter-cor').value;
    const ano = document.getElementById('filter-ano').value;

    const params = new URLSearchParams({ tipo, modelo, cor, ano }).toString();
    const response = await fetch(`${apiBaseUrl}/veiculos/busca-especifica?${params}`);
    const veiculos = await response.json();
    const tableBody = document.getElementById('veiculos-table-body');
    tableBody.innerHTML = '';

    veiculos.forEach(veiculo => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${veiculo.id}</td>
            <td>${veiculo.tipo}</td>
            <td>${veiculo.modelo}</td>
            <td>${veiculo.cor}</td>
            <td>${veiculo.ano}</td>
            <td class="actions">
                <button class="edit" onclick="editarVeiculo(${veiculo.id})">Editar</button>
                <button class="delete" onclick="deletarVeiculo(${veiculo.id})">Excluir</button>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

function abrirFormularioCadastro() {
    Swal.fire({
        title: 'Cadastrar Veículo',
        html: `
            <label for="tipo-veiculo">Tipo:</label>
            <select id="tipo-veiculo" onchange="atualizarFormularioCadastro()">
                <option value="">Selecione</option>
                <option value="Carro">Carro</option>
                <option value="Moto">Moto</option>
            </select>
            <br>
            <label for="modelo">Modelo:</label>
            <input type="text" id="modelo" placeholder="Modelo">
            <label for="fabricante">Fabricante:</label>
            <input type="text" id="fabricante" placeholder="Fabricante">
            <label for="ano">Ano:</label>
            <input type="number" id="ano" placeholder="Ano">
            <label for="preco">Preço:</label>
            <input type="number" id="preco" placeholder="Preço">
            <div id="campos-extras"></div>
        `,
        showCancelButton: true,
        confirmButtonText: 'Salvar',
        preConfirm: () => {
            const tipo = document.getElementById('tipo-veiculo').value;
            const modelo = document.getElementById('modelo').value;
            const fabricante = document.getElementById('fabricante').value;
            const ano = document.getElementById('ano').value;
            const preco = document.getElementById('preco').value;

            let extras = {};
            if (tipo === 'Carro') {
                extras.quantidadePortas = document.getElementById('quantidade-portas').value;
                extras.tipoCombustivel = document.getElementById('tipo-combustivel').value;
            } else if (tipo === 'Moto') {
                extras.cilindrada = document.getElementById('cilindrada').value;
            }

            return { tipo, modelo, fabricante, ano, preco, ...extras };
        }
    }).then(result => {
        if (result.isConfirmed) {
            const dados = result.value;
            if (tipo == 'Carro') {
                url = `${apiBaseUrl}/veiculos/cadastrar-carro`
            } else {
                url = `${apiBaseUrl}/veiculos/cadastrar-moto`
            }
            fetch(`${url}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(dados)
            }).then(() => carregarVeiculos());
        }
    });
}

function atualizarFormularioCadastro() {
    const tipo = document.getElementById('tipo-veiculo').value;
    const camposExtras = document.getElementById('campos-extras');
    camposExtras.innerHTML = '';

    if (tipo === 'Carro') {
        camposExtras.innerHTML = `
            <label for="quantidade-portas">Quantidade de Portas:</label>
            <input type="number" id="quantidade-portas" placeholder="Portas">
            <label for="tipo-combustivel">Tipo de Combustível:</label>
            <select id="tipo-combustivel">
                <option value="Gasolina">Gasolina</option>
                <option value="Etanol">Etanol</option>
                <option value="Flex">Flex</option>
                <option value="Diesel">Diesel</option>
            </select>
        `;
    } else if (tipo === 'Moto') {
        camposExtras.innerHTML = `
            <label for="cilindrada">Cilindrada:</label>
            <input type="number" id="cilindrada" placeholder="Cilindrada">
        `;
    }
}

function editarVeiculo(id) {
    alert(`Função de editar veículo ainda não implementada para o ID ${id}`);
}

function deletarVeiculo(id) {
    if (confirm('Tem certeza que deseja excluir este veículo?')) {
        fetch(`${apiBaseUrl}/deletar/${id}`, { method: 'DELETE' })
            .then(() => carregarVeiculos());
    }
}
