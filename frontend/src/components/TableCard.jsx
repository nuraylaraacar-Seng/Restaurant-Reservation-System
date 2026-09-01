export default function TableCard({ table, onReserve, canManage }) {
    return (
        <div className={`table-card ${!table.active ? "table-card--inactive" : ""}`}>
            <div className="table-card__top">
                <span className="table-card__number">Masa {table.tableNumber}</span>
                <span className={`table-card__status ${table.active ? "is-active" : "is-inactive"}`}>
          {table.active ? "Uygun" : "Pasif"}
        </span>
            </div>

            <div className="table-card__body">
                <p>{table.capacity} kişilik</p>
                {table.location && <p className="table-card__location">{table.location}</p>}
            </div>

            <button
                className="button button--primary button--full"
                disabled={!table.active}
                onClick={() => onReserve(table)}
            >
                Rezerve Et
            </button>
        </div>
    );
}
